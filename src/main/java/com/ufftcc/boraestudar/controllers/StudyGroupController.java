package com.ufftcc.boraestudar.controllers;

import com.ufftcc.boraestudar.mappers.StudyGroupMapper;
import com.ufftcc.boraestudar.dtos.studygroup.StudyGroupCreateDto;
import com.ufftcc.boraestudar.dtos.studygroup.StudyGroupResponseDto;
import com.ufftcc.boraestudar.dtos.studygroup.StudyGroupFilterDto;
import com.ufftcc.boraestudar.dtos.studygroup.StudyGroupUpdateDto;
import com.ufftcc.boraestudar.dtos.studygroupuser.RegisterUserToGroupDto;
import com.ufftcc.boraestudar.entities.StudyGroup;
import com.ufftcc.boraestudar.services.StudyGroupService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/study-groups")
public class StudyGroupController {

    private final StudyGroupService service;
    private final StudyGroupMapper mapper;

    public StudyGroupController(StudyGroupService service, StudyGroupMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudyGroupResponseDto save(@Valid @RequestBody StudyGroupCreateDto dto) {
        StudyGroup createdStudyGroup = service.create(dto);
        return mapper.toTransferObject(createdStudyGroup, StudyGroupResponseDto.class);
    }

    @PostMapping("/filter")
    @ResponseStatus(HttpStatus.OK)
    public List<StudyGroupResponseDto> findAll(@RequestBody Optional<StudyGroupFilterDto> dto) {
        List<StudyGroup> studyGroups = service.findAll(dto);
        return mapper.toTransferObjectList(studyGroups, StudyGroupResponseDto.class);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StudyGroupResponseDto findById(@PathVariable Long id) {
        StudyGroup studyGroup = service.findById(id);
        return mapper.toTransferObject(studyGroup, StudyGroupResponseDto.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StudyGroupResponseDto updateById(@PathVariable Long id, @RequestBody StudyGroupUpdateDto dto) {
        StudyGroup updatedUser = service.updateById(id, dto);
        return mapper.toTransferObject(updatedUser, StudyGroupResponseDto.class);
    }

    @PostMapping("/{groupId}/students/join")
    @ResponseStatus(HttpStatus.OK)
    public void registerUserToGroup(@PathVariable Long groupId, @RequestBody RegisterUserToGroupDto dto) {
        service.registerUserToGroup(groupId, dto);
    }

    @PostMapping("/{groupId}/students/{studentId}/leave")
    public void removeStudentFromGroup(@PathVariable Long groupId, @PathVariable Long studentId) {
        service.removeStudentFromGroup(groupId, studentId);
    }

    @PostMapping("/{groupId}/tutors/{tutorId}/leave")
    public void removeTutorFromGroup(@PathVariable Long groupId, @PathVariable Long tutorId) {
        service.removeTutorFromGroup(groupId, tutorId);
    }

//    @DeleteMapping("/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void deleteById(@PathVariable Long id) {
//        service.deleteById(id);
//    }

}
