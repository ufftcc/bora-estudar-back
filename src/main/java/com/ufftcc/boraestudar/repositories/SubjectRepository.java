package com.ufftcc.boraestudar.repositories;

import com.ufftcc.boraestudar.entities.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    @Query(value = "SELECT * FROM SUBJECT sub ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Subject findRandomSubject();

}
