package com.ufftcc.boraestudar.controllers;

import com.ufftcc.boraestudar.mappers.StudyGroupMapper;
import com.ufftcc.boraestudar.dtos.studygroup.StudyGroupCreateDto;
import com.ufftcc.boraestudar.dtos.studygroup.StudyGroupResponseDto;
import com.ufftcc.boraestudar.dtos.studygroup.StudyGroupFilterDto;
import com.ufftcc.boraestudar.dtos.studygroup.StudyGroupUpdateDto;
import com.ufftcc.boraestudar.entities.StudyGroup;
import com.ufftcc.boraestudar.services.DiscordBotService;
import com.ufftcc.boraestudar.services.StudyGroupService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/study-groups")
public class StudyGroupController {

    private final StudyGroupService studyGroupService;
    private final StudyGroupMapper mapper;
    private final DiscordBotService discordBotService;
    private static final Logger log = LoggerFactory.getLogger(DiscordBotService.class);

    public StudyGroupController(StudyGroupService service, StudyGroupMapper mapper, DiscordBotService discordBotService) {
        this.studyGroupService = service;
        this.mapper = mapper;
        this.discordBotService = discordBotService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudyGroupResponseDto save(@Valid @RequestBody StudyGroupCreateDto dto) {
        StudyGroup createdStudyGroup = studyGroupService.create(dto);
        discordBotService.createStudyGroupServer(createdStudyGroup, dto)
                .doOnSuccess(discordId -> log.info("ID da role: " + discordId))
                .subscribe();

        return mapper.toTransferObject(createdStudyGroup, StudyGroupResponseDto.class);
    }

    @PostMapping("/filter")
    @ResponseStatus(HttpStatus.OK)
    public List<StudyGroupResponseDto> findAll(@RequestBody Optional<StudyGroupFilterDto> dto) {
        List<StudyGroup> studyGroups = studyGroupService.findAll(dto);
        return mapper.toTransferObjectList(studyGroups, StudyGroupResponseDto.class);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StudyGroupResponseDto findById(@PathVariable Long id) {
        StudyGroup studyGroup = studyGroupService.findById(id);
        return mapper.toTransferObject(studyGroup, StudyGroupResponseDto.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StudyGroupResponseDto updateById(@PathVariable Long id, @RequestBody StudyGroupUpdateDto dto) {
        StudyGroup updatedUser = studyGroupService.updateById(id, dto);
        return mapper.toTransferObject(updatedUser, StudyGroupResponseDto.class);
    }

    @PostMapping("/{groupId}/students/{studentId}/join")
    @ResponseStatus(HttpStatus.OK)
    public void registerUserToGroup(@PathVariable Long groupId, @PathVariable Long studentId) {
        studyGroupService.registerUserToGroup(groupId, studentId);
    }

    @PostMapping("/{groupId}/students/{studentId}/leave")
    public void removeStudentFromGroup(@PathVariable Long groupId, @PathVariable Long studentId) {
        studyGroupService.removeStudentFromGroup(groupId, studentId);
    }

//    @DeleteMapping("/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void deleteById(@PathVariable Long id) {
//        service.deleteById(id);
//    }

}
