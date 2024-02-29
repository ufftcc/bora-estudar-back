package com.ufftcc.boraestudar.repositories;

import com.ufftcc.boraestudar.entities.StudyGroupUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudyGroupUserRepository extends JpaRepository<StudyGroupUser, Long> {

    Boolean existsByUserIdAndStudyGroupId(Long userId, Long studyGroupId);

    void deleteByUserIdAndStudyGroupId(Long studentId, Long id);

    Optional<StudyGroupUser> findByUserIdAndStudyGroupId(Long studentId, Long studyGroupId);
}
