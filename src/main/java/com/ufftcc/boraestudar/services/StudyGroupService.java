package com.ufftcc.boraestudar.services;

import com.ufftcc.boraestudar.mappers.StudyGroupMapper;
import com.ufftcc.boraestudar.dtos.studygroup.StudyGroupCreateDto;
import com.ufftcc.boraestudar.dtos.studygroup.StudyGroupFilterDto;
import com.ufftcc.boraestudar.dtos.studygroup.StudyGroupUpdateDto;
import com.ufftcc.boraestudar.entities.*;
import com.ufftcc.boraestudar.exceptions.studygroup.InsufficientPrivilegesException;
import com.ufftcc.boraestudar.exceptions.studygroup.NoStudentsSlotsAvailableException;
import com.ufftcc.boraestudar.exceptions.studygroup.StudyGroupNotFoundException;
import com.ufftcc.boraestudar.repositories.StudyGroupRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        registerUserToGroup(createdStudyGroup.getId(), dto.getOwnerId());

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
        return repository.findByAttributesDinamicQuery(dto.description(), dto.title(), dto.subjectName(), dto.meetingTime(), dto.weekdays(), dto.modality(), dto.studentId());
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
        studyGroup = copyProperties(grupoEstudoEncontrado, studyGroup);

        return repository.save(studyGroup);
    }

    private StudyGroup copyProperties(StudyGroup persistedStudyGroup, StudyGroup receivedStudyGroup) {
        if (receivedStudyGroup.getTitle() != null) {
            persistedStudyGroup.setTitle(receivedStudyGroup.getTitle());
        }

        if (receivedStudyGroup.getDescription() != null) {
            persistedStudyGroup.setDescription(receivedStudyGroup.getDescription());
        }

        if (receivedStudyGroup.getMeetingTime() != null) {
            persistedStudyGroup.setMeetingTime(receivedStudyGroup.getMeetingTime());
        }

        if (receivedStudyGroup.getStudyGroupWeekdays() != null && !receivedStudyGroup.getStudyGroupWeekdays().isEmpty()) {
            List<Weekday> receivedWeekDays = receivedStudyGroup.getStudyGroupWeekdays().stream()
                    .map(studyGroupWeekday -> studyGroupWeekday.getWeekday())
                    .collect(Collectors.toList());

            persistedStudyGroup.getStudyGroupWeekdays()
                    .removeIf(studyGroupWeekday -> !receivedWeekDays.contains(studyGroupWeekday.getWeekday()));

            List<Weekday> persistedWeekDays = persistedStudyGroup.getStudyGroupWeekdays().stream()
                    .map(studyGroupWeekday -> studyGroupWeekday.getWeekday())
                    .collect(Collectors.toList());

            receivedStudyGroup.getStudyGroupWeekdays()
                    .removeIf(studyGroupWeekday -> persistedWeekDays.contains(studyGroupWeekday.getWeekday()));

            receivedStudyGroup.getStudyGroupWeekdays().forEach(received -> {
                received.setStudyGroup(persistedStudyGroup);
                persistedStudyGroup.addStudyGroupWeekdays(received);
            });
        }

        if (receivedStudyGroup.getDiscordId() != null) {
            persistedStudyGroup.setDiscordId(receivedStudyGroup.getDiscordId());
        }

        return persistedStudyGroup;
    }

    public Boolean isOwner(Long groupId, Long ownerId) {
        return repository.isOwner(groupId, ownerId);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public void registerUserToGroup(Long groupStudyId, Long studentId) {
        StudyGroup studyGroup = findById(groupStudyId);

        if (!studyGroup.hasStudentSlotsAvailable()) {
            throw new NoStudentsSlotsAvailableException("Nao ha vaga para estudantes no Grupo de estudo " + studyGroup.getId());
        }
        User user = userService.findById(studentId);
        studyGroupUserService.registerUserToGroup(studyGroup, user);
    }

    @Transactional
    public void removeStudentFromGroup(Long studyGroupId, Long studentId) {
        StudyGroup studyGroup = findById(studyGroupId);
        StudyGroupUser studyGroupUser = studyGroupUserService.findByUserIdAndStudyGroupId(studentId, studyGroup.getId());
        studyGroupUserService.removeStudentFromGroup(studyGroupUser);

        if (studyGroup.getOwnerId().equals(studyGroupUser.getUser().getId())) {
            setNewOwnerToGroup(studyGroup);
        }

        studyGroup.removeStudent(studyGroupUser);
        repository.save(studyGroup);

        tryDeleteStudyGroup(studyGroup);
    }

    private void setNewOwnerToGroup(StudyGroup studyGroup) {
        Long newOwnerId = null;
        if (!studyGroup.getStudents().isEmpty()) {
            newOwnerId = studyGroup.getStudents().get(0).getUser().getId();
        }
        studyGroup.setOwnerId(newOwnerId);
    }

    private void tryDeleteStudyGroup(StudyGroup studyGroup) {
        if (studyGroup.getStudents().size() == 0) {
            repository.delete(studyGroup);
        }
    }
}
