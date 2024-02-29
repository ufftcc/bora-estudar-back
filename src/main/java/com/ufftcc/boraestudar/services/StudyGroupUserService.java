package com.ufftcc.boraestudar.services;

import com.ufftcc.boraestudar.entities.StudyGroup;
import com.ufftcc.boraestudar.entities.StudyGroupUser;
import com.ufftcc.boraestudar.entities.User;
import com.ufftcc.boraestudar.exceptions.study_group.UserAlreadyRegisteredException;
import com.ufftcc.boraestudar.repositories.StudyGroupUserRepository;
import org.springframework.stereotype.Service;

@Service
public class StudyGroupUserService {

    private final StudyGroupUserRepository repository;

    public StudyGroupUserService(StudyGroupUserRepository repository) {
        this.repository = repository;
    }

    public void registerStudentToGroup(StudyGroup studyGroup, User user) {
        existsByUserIdAndStudyGroupId(user.getId(), studyGroup.getId());
        StudyGroupUser studyGroupUser = new StudyGroupUser();
        studyGroupUser.setUser(user);
        studyGroupUser.setStudyGroup(studyGroup);
        studyGroupUser = repository.save(studyGroupUser);
        studyGroup.addStudent(studyGroupUser);
        //TODO fazer função .add em StudyGroup
    }

    public void removeStudentFromGroup(StudyGroupUser studyGroupUser) {
        repository.delete(studyGroupUser);
    }

    public void existsByUserIdAndStudyGroupId(Long userId, Long studyGroupId) {
        if (!repository.existsByUserIdAndStudyGroupId(userId, studyGroupId)) {
            //TODO: criar exceção para usuário já cadastrado no grupo
            throw new RuntimeException("Usuário não cadastrado no grupo de estudo");
        }
    }

    public StudyGroupUser findByUserIdAndStudyGroupId(Long studentId, Long studyGroupId) {
        //TODO: criar exceção para usuário não cadastrado no grupo
        return repository.findByUserIdAndStudyGroupId(studentId, studyGroupId).orElseThrow(() -> new RuntimeException("Usuário não cadastrado no grupo de estudo"));
    }
}
