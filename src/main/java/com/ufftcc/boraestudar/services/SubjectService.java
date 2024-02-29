package com.ufftcc.boraestudar.services;

import com.ufftcc.boraestudar.dto.mapper.SubjectMapper;
import com.ufftcc.boraestudar.dto.subject.SubjectUpdateDto;
import com.ufftcc.boraestudar.dto.subject.SubjectCreateDto;
import com.ufftcc.boraestudar.entities.Subject;
import com.ufftcc.boraestudar.exceptions.subject.SubjectNotFoundException;
import com.ufftcc.boraestudar.repositories.SubjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {

    private final SubjectRepository repository;
    private final SubjectMapper mapper;

    public SubjectService(SubjectRepository repository, SubjectMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Subject create(SubjectCreateDto dto) {
        Subject subject = mapper.toEntity(dto);
        return  repository.save(subject);
    }

    public Subject findById(Long id) {
        Optional<Subject> disciplinaEncontrada = repository.findById(id);
        if (disciplinaEncontrada.isEmpty()) {
            throw new SubjectNotFoundException(id);
        }
        return disciplinaEncontrada.get();
    }

    public List<Subject> findAll() {
        return repository.findAll();
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public Subject updateById(Long id, SubjectUpdateDto subjectUpdateDto) {
        Optional<Subject> subjectFound = repository.findById(id);
        if (subjectFound.isEmpty()){
            throw new SubjectNotFoundException(id);
        }

        Subject subject = mapper.toEntity(subjectUpdateDto);
        subject.setId(id);
        return repository.save(subject);
    }
}
