package com.ufftcc.boraestudar.controllers;

import com.ufftcc.boraestudar.dto.mapper.SubjectMapper;
import com.ufftcc.boraestudar.dto.subject.SubjectCreateDto;
import com.ufftcc.boraestudar.dto.subject.SubjectResponseDto;
import com.ufftcc.boraestudar.dto.subject.SubjectUpdateDto;
import com.ufftcc.boraestudar.entities.Subject;
import com.ufftcc.boraestudar.services.SubjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subjects")
public class SubjectController {

    private final SubjectService service;
    private SubjectMapper mapper;

    SubjectController(SubjectService service, SubjectMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubjectResponseDto save(@Valid @RequestBody SubjectCreateDto dto) {
        Subject createdSubject = service.create(dto);
        return mapper.toTransferObject(createdSubject, SubjectResponseDto.class);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<SubjectResponseDto> findAll() {
        List<Subject> subjects = service.findAll();
        return mapper.toTransferObjectList(subjects, SubjectResponseDto.class);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SubjectResponseDto find(@PathVariable Long id) {
        Subject subject = service.findById(id);
        return mapper.toTransferObject(subject, SubjectResponseDto.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SubjectResponseDto updateById(@PathVariable Long id, @RequestBody SubjectUpdateDto dto) {
        Subject updatedSubject = service.updateById(id, dto);
        return mapper.toTransferObject(updatedSubject, SubjectResponseDto.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }
}
