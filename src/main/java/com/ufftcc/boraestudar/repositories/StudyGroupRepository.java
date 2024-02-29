package com.ufftcc.boraestudar.repositories;

import com.ufftcc.boraestudar.entities.StudyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyGroupRepository extends JpaRepository<StudyGroup, Long> {

    @Query("SELECT EXISTS (SELECT 1 FROM StudyGroup sg WHERE sg.id = :groupId AND sg.ownerId = :ownerId)")
    Boolean isOwner(Long groupId, Long ownerId);

}
