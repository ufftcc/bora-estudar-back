package com.ufftcc.boraestudar.services;

import com.ufftcc.boraestudar.dto.mapper.StudyGroupMapper;
import com.ufftcc.boraestudar.dto.study_group.StudyGroupCreateDto;
import com.ufftcc.boraestudar.dto.study_group.StudyGroupFilterDto;
import com.ufftcc.boraestudar.dto.study_group.StudyGroupUpdateDto;
import com.ufftcc.boraestudar.dto.study_group_user.RegisterUserToGroupDto;
import com.ufftcc.boraestudar.entities.*;
import com.ufftcc.boraestudar.exceptions.InsufficientPrivilegesException;
import com.ufftcc.boraestudar.exceptions.study_group.NoStudentsSlotsAvailableException;
import com.ufftcc.boraestudar.exceptions.study_group.StudyGroupNotFoundException;
import com.ufftcc.boraestudar.exceptions.study_group.TutorAlreadyRegisteredException;
import com.ufftcc.boraestudar.repositories.StudyGroupRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudyGroupService {

    private final UserService userService;
    private final StudyGroupUserService studyGroupUserService;
    private final StudyGroupRepository repository;
    private final StudyGroupMapper mapper;

    public StudyGroupService(UserService userService, StudyGroupUserService studyGroupUserService,
                             StudyGroupRepository repository, StudyGroupMapper mapper) {
        this.userService = userService;
        this.studyGroupUserService = studyGroupUserService;
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public StudyGroup create(StudyGroupCreateDto dto) {
        // TODO: verificar a seguinte regra --> um usuário pode criar vários grupos para uma mesma disciplina? --> Sim, contanto que sejam dias/horarios diferentes?
        StudyGroup studyGroup = mapper.toEntity(dto);
        studyGroup.getStudyGroupWeekdays().forEach(studyGroupWeekday -> studyGroupWeekday.setStudyGroup(studyGroup));
        StudyGroup createdStudyGroup = repository.save(studyGroup);

        RegisterUserToGroupDto registerUserToGroupDto = new RegisterUserToGroupDto();
        registerUserToGroupDto.setUserId(dto.getOwnerId());
        registerUserToGroupDto.setIsTutor(dto.hasTutor());

        registerUserToGroup(createdStudyGroup, registerUserToGroupDto);

        return createdStudyGroup;
    }

    public StudyGroup findById(Long id) {
        Optional<StudyGroup> studyGroup = repository.findById(id);
        if (studyGroup.isEmpty()) {
            throw new StudyGroupNotFoundException(id);
        }
        return studyGroup.get();
    }

    public List<StudyGroup> findAll(Optional<StudyGroupFilterDto> filterDto) {
        if (filterDto.isEmpty()) {
            return findAll();
        }

        StudyGroupFilterDto dto = filterDto.get();
        return repository.findByAttributesDinamicQuery(dto.description(), dto.title(), dto.subjectName(), dto.meetingTime(), dto.weekdays());
    }

    public List<StudyGroup> findAll() {
        return repository.findAll();
    }

    @Transactional
    public StudyGroup updateById(Long groupId, StudyGroupUpdateDto dto) {
        StudyGroup grupoEstudoEncontrado = findById(groupId);

        if (!isOwner(groupId, dto.getUserId())) {
            //TODO criar exceção para usuário não ser o dono do grupo e lançar HttpStatus.FORBIDDEN
            throw new InsufficientPrivilegesException("Usuario nao possui permissao para alterar o grupo de estudo");
        }

        StudyGroup studyGroup = mapper.toEntity(dto);
        String[] propriedadesIgnoradas = {"title", "description"};
        BeanUtils.copyProperties(grupoEstudoEncontrado, studyGroup, propriedadesIgnoradas);

        return repository.save(studyGroup);
    }

    public Boolean isOwner(Long groupId, Long ownerId) {
        return repository.isOwner(groupId, ownerId);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public void registerUserToGroup(Long groupStudyId, RegisterUserToGroupDto dto) {
        StudyGroup studyGroup = findById(groupStudyId);
        registerUserToGroup(studyGroup, dto);
    }

    public void registerUserToGroup(StudyGroup studyGroup, RegisterUserToGroupDto dto) {
        if(dto.getIsTutor()){
            registerTutor(dto, studyGroup);
            return;
        }
        registerStudent(dto, studyGroup);
    }

    private void registerTutor(RegisterUserToGroupDto dto, StudyGroup studyGroup) {
        if(dto.getIsTutor() && studyGroup.getTutor() != null) {
            throw new TutorAlreadyRegisteredException("Nao ha vaga de tutor disponivel");
        } else {
            User user = userService.findById(dto.getUserId());
            studyGroup.setTutor(user);
            repository.save(studyGroup);
        }
    }

    private void registerStudent(RegisterUserToGroupDto dto, StudyGroup studyGroup) {
        if (!studyGroup.hasStudentSlotsAvailable()) {
            throw new NoStudentsSlotsAvailableException("Nao ha vaga para estudantes no Grupo de estudo " + studyGroup.getId());
        }
        User user = userService.findById(dto.getUserId());
        studyGroupUserService.registerStudentToGroup(studyGroup, user);
    }

    @Transactional
    public void removeStudentFromGroup(Long studyGroupId, Long studentId) {
        StudyGroup studyGroup = findById(studyGroupId);
        StudyGroupUser studyGroupUser = studyGroupUserService.findByUserIdAndStudyGroupId(studentId, studyGroup.getId());
        studyGroupUserService.removeStudentFromGroup(studyGroupUser);

        if (studyGroup.getOwnerId().equals(studyGroupUser.getUser().getId())) {
            setNewOwnerToGroup(studyGroup, true);
        }

        studyGroup.removeStudent(studyGroupUser);
        repository.save(studyGroup);

        tryDeleteStudyGroup(studyGroup);
    }

    public void removeTutorFromGroup(Long studyGroupId, Long tutorId) {
        StudyGroup studyGroup = findById(studyGroupId);

        if(!studyGroup.getTutor().getId().equals(tutorId)) {
            //TODO criar exceção para tutor não ser o tutor do grupo
            throw new RuntimeException("Usuario nao é tutor do grupo de estudo " + studyGroupId);
        }

        if (studyGroup.getOwnerId().equals(tutorId)) {
            setNewOwnerToGroup(studyGroup, false);
        }

        studyGroup.setTutor(null);
        repository.save(studyGroup);
        tryDeleteStudyGroup(studyGroup);
    }

    private void setNewOwnerToGroup(StudyGroup studyGroup, Boolean useTutor) {
        Long newOwnerId = null;
        if (useTutor && studyGroup.hasTutor()) {
            newOwnerId = studyGroup.getTutor().getId();
        } else if (!studyGroup.getStudents().isEmpty()) {
            newOwnerId = studyGroup.getStudents().get(0).getUser().getId();
        }
        studyGroup.setOwnerId(newOwnerId);
    }

    private void tryDeleteStudyGroup(StudyGroup studyGroup) {
        if (studyGroup.getStudents().size() == 0 && !studyGroup.hasTutor()) {
            repository.delete(studyGroup);
        }
    }
}
