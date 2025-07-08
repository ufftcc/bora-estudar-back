package com.ufftcc.boraestudar.controllers;

import com.ufftcc.boraestudar.entities.User;
import com.ufftcc.boraestudar.mappers.StudyGroupMapper;
import com.ufftcc.boraestudar.dtos.studygroup.StudyGroupCreateDto;
import com.ufftcc.boraestudar.dtos.studygroup.StudyGroupResponseDto;
import com.ufftcc.boraestudar.dtos.studygroup.StudyGroupFilterDto;
import com.ufftcc.boraestudar.dtos.studygroup.StudyGroupUpdateDto;
import com.ufftcc.boraestudar.entities.StudyGroup;
import com.ufftcc.boraestudar.services.*;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/study-groups")
public class StudyGroupController {

    private final StudyGroupService studyGroupService;
    private final StudyGroupMapper mapper;
    private final DiscordBotService discordBotService;
    private final UserService userService;
    private final StudyGroupRandomCreationService randomCreationService;
    private static final Logger log = LoggerFactory.getLogger(DiscordBotService.class);


    public StudyGroupController(StudyGroupService service, StudyGroupMapper mapper, DiscordBotService discordBotService, UserService userService, StudyGroupRandomCreationService randomCreationService) {
        this.studyGroupService = service;
        this.mapper = mapper;
        this.discordBotService = discordBotService;
        this.userService = userService;
        this.randomCreationService = randomCreationService;
    }

    @PostMapping("/criar-aleatorio")
    @ResponseStatus(HttpStatus.CREATED)
    public StudyGroupResponseDto criarGrupoAleatorio() {
        StudyGroupCreateDto dtoAleatorio = randomCreationService.montarDtoAleatorio();
        return save(dtoAleatorio);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudyGroupResponseDto save(@Valid @RequestBody StudyGroupCreateDto dto) {
        StudyGroup createdStudyGroup = studyGroupService.create(dto);
        discordBotService.createStudyGroupServer(createdStudyGroup, dto)
                .doOnSuccess(discordOperationResult -> {
                    log.info("ID da role: " + discordOperationResult.getRoleId());
                    log.info("Invite: " + discordOperationResult.getInviteUrl());
                    createdStudyGroup.setDiscordId(discordOperationResult.getRoleId());
                    createdStudyGroup.setDiscordInviteUrl(discordOperationResult.getInviteUrl());
                    studyGroupService.updateByIdOnGroupCreation(createdStudyGroup);

                    Long userDiscordId = userService.findById(dto.getOwnerId()).getDiscordId();

                    discordBotService.registerUserToRole(userDiscordId, createdStudyGroup.getDiscordId());

                })
                .subscribe();

        return mapper.toTransferObject(createdStudyGroup, StudyGroupResponseDto.class);
    }

 /*   @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<StudyGroupResponseDto> save(@Valid @RequestBody StudyGroupCreateDto dto) {
        return Mono.fromCallable(() -> studyGroupService.create(dto))
                .flatMap(createdStudyGroup -> {
                    return discordBotService.createStudyGroupServer(createdStudyGroup, dto)
                            .flatMap(discordResult -> {
                                createdStudyGroup.setDiscordId(discordResult.getRoleId());
                                createdStudyGroup.setDiscordInviteUrl(discordResult.getInviteUrl());

                                return Mono.fromCallable(() -> studyGroupService.updateByIdOnGroupCreation(createdStudyGroup))
                                        .flatMap(updatedGroup -> {
                                            Long userDiscordId = userService.findById(dto.getOwnerId()).getDiscordId();

                                            return Mono.fromRunnable(() ->
                                                            discordBotService.registerUserToRole(userDiscordId, discordResult.getRoleId())
                                                    )
                                                    .subscribeOn(Schedulers.boundedElastic())
                                                    .onErrorResume(e -> {
                                                        log.error("Falha ao registrar usuário no Discord (não crítico): {}", e.getMessage());
                                                        return Mono.empty(); // Continua mesmo com falha no Discord
                                                    })
                                                    .thenReturn(updatedGroup);
                                        });
                            })
                            .map(updatedGroup -> mapper.toTransferObject(updatedGroup, StudyGroupResponseDto.class));
                })
                .doOnSuccess(response -> log.info("Grupo criado com sucesso: {}", response))
                .doOnError(e -> log.error("Erro crítico ao criar grupo", e))
                .onErrorResume(e -> {
                    if (e instanceof ResponseStatusException) {
                        return Mono.error(e);
                    }

                    // Erros genéricos
                    return Mono.error(new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "Erro ao criar grupo de estudo: " + e.getMessage(), e));
                });
    }*/

//    @PostMapping("/filter")
//    @ResponseStatus(HttpStatus.OK)
//    public List<StudyGroupResponseDto> findAll(@RequestBody Optional<StudyGroupFilterDto> dto) {
//        List<StudyGroup> studyGroups = studyGroupService.findAll(dto);
//        studyGroups.sort(Comparator.comparing(StudyGroup::getTitle));
//        return mapper.toTransferObjectList(studyGroups, StudyGroupResponseDto.class);
//    }

    @PostMapping("/filter")
    @ResponseStatus(HttpStatus.OK)
    public List<StudyGroupResponseDto> findAll(@RequestBody Optional<StudyGroupFilterDto> dto) {
        List<StudyGroup> studyGroups = studyGroupService.findAll(dto);
        studyGroups.sort(Comparator.comparing(sg -> {
            String title = sg.getSubject().getName();
            if (title == null) {
                return "";
            }
            // Remove accents and non-ASCII characters, then lowercase
            String normalized = java.text.Normalizer.normalize(title, java.text.Normalizer.Form.NFD);
            String withoutAccents = normalized.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
            String asciiOnly = withoutAccents.replaceAll("[^\\p{ASCII}]", "");
            return asciiOnly.toLowerCase();
        }));
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

        StudyGroup studyGroup = studyGroupService.findById(groupId);
        User user = userService.findById(studentId);

        discordBotService.registerUserToRole(user.getDiscordId(), studyGroup.getDiscordId());
    }

    @PostMapping("/{groupId}/students/{studentId}/leave")
    public void removeStudentFromGroup(@PathVariable Long groupId, @PathVariable Long studentId) {

        StudyGroup studyGroup = studyGroupService.findById(groupId);
        User user = userService.findById(studentId);

        studyGroupService.removeStudentFromGroup(groupId, studentId);
        discordBotService.removeUserFromRole(user.getDiscordId(), studyGroup.getDiscordId());
    }

//    @DeleteMapping("/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void deleteById(@PathVariable Long id) {
//        service.deleteById(id);
//    }

//    @PostMapping("/invite-guild")
//    @ResponseStatus(HttpStatus.CREATED)
//    public String criarInviteParaServidor() {
//        return discordBotService.createInviteGuild();
//    }


}
