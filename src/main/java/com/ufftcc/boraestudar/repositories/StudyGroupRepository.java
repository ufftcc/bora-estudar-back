package com.ufftcc.boraestudar.repositories;

import com.ufftcc.boraestudar.entities.StudyGroup;
import com.ufftcc.boraestudar.entities.Weekday;
import com.ufftcc.boraestudar.enums.ModalityEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface StudyGroupRepository extends JpaRepository<StudyGroup, Long>, JpaSpecificationExecutor<StudyGroup> {

    @Query("SELECT EXISTS (SELECT 1 FROM StudyGroup sg WHERE sg.id = :groupId AND sg.ownerId = :ownerId)")
    Boolean isOwner(Long groupId, Long ownerId);

    @Query("SELECT sg FROM StudyGroup sg " +
            "INNER JOIN sg.studyGroupWeekdays sgw " +
            "INNER join sgw.weekday w " +
            "INNER join sg.subject su " +
            "INNER join sg.students st " +
            "WHERE (:title is null OR sg.title = :title) " +
            "AND (:description is null OR sg.description LIKE %:description%) " +
            "AND (:subjectName is null OR su.name LIKE %:subjectName%) " +
            "AND (:weekdaysId is null OR w.id IN :weekdaysId)" +
            "AND (:modality is null OR sg.modality = :modality) " +
            "AND (:meetingTime is null OR sg.meetingTime = :meetingTime) " +
            "AND (:studentId is null OR st.user.id = :studentId) ")
    List<StudyGroup> findByAttributesDinamicQuery(String description, String title, String subjectName,
                                                  LocalTime meetingTime, List<Long> weekdaysId, ModalityEnum modality, Long studentId);

}
