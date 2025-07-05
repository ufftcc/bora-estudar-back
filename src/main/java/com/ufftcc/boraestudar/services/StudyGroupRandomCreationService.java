package com.ufftcc.boraestudar.services;

import com.ufftcc.boraestudar.dtos.studygroup.StudyGroupCreateDto;
import com.ufftcc.boraestudar.dtos.subject.SubjectResponseDto;
import com.ufftcc.boraestudar.entities.Subject;
import com.ufftcc.boraestudar.entities.User;
import com.ufftcc.boraestudar.entities.Weekday;
import com.ufftcc.boraestudar.enums.ModalityEnum;
import com.ufftcc.boraestudar.repositories.SubjectRepository;
import com.ufftcc.boraestudar.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;

@Service
public class StudyGroupRandomCreationService {

    private final StudyGroupService studyGroupService;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final Random random = new Random();

    public StudyGroupRandomCreationService(StudyGroupService studyGroupService,
                                           UserRepository userRepository,
                                           SubjectRepository subjectRepository) {
        this.studyGroupService = studyGroupService;
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
    }

    private static final List<Weekday> TODOS_OS_DIAS = List.of(
            criarWeekday(1L, "Segunda-feira"),
            criarWeekday(2L, "Terça-feira"),
            criarWeekday(3L, "Quarta-feira"),
            criarWeekday(4L, "Quinta-feira"),
            criarWeekday(5L, "Sexta-feira"),
            criarWeekday(6L, "Sábado"),
            criarWeekday(7L, "Domingo")
    );

    private static Weekday criarWeekday(Long id, String nome) {
        Weekday w = new Weekday();
        w.setId(id);
        w.setName(nome);
        return w;
    }

    public StudyGroupCreateDto montarDtoAleatorio() {


        StudyGroupCreateDto createStudyGroup = new StudyGroupCreateDto();

        // Buscar um usuário aleatório como owner
        User owner = userRepository.findRandomUser(); // você precisa implementar findRandomUser()

        // Buscar um subject aleatório
        Subject subject = subjectRepository.findRandomSubject(); // idem para findRandomSubject()

        // Montar SubjectResponseDto (você deve adaptar conforme seu mapper)
        SubjectResponseDto subjectDto = new SubjectResponseDto();
        subjectDto.setId(subject.getId());
        subjectDto.setCode(subject.getCode());
        subjectDto.setName(subject.getName());
        // ... preencher outros campos do SubjectResponseDto conforme necessário

        createStudyGroup.setOwnerId(owner.getId());
        createStudyGroup.setSubject(subjectDto);

        // Título e descrição aleatórios
        createStudyGroup.setTitle("Grupo de estudo sobre " + subject.getName() + " #" + random.nextInt(1000));
        createStudyGroup.setDescription("Descrição aleatória gerada para o grupo de estudo.");

        createStudyGroup.setMaxStudents(2 + random.nextInt(5)); // 2 + [0..4] = 2 a 6

        // Horário da reunião aleatório, por exemplo entre 8h e 20h
        int hour = 8 + random.nextInt(13);
        int minute = random.nextBoolean() ? 0 : 30;
        createStudyGroup.setMeetingTime(LocalTime.of(hour, minute));

        List<Weekday> copia = new ArrayList<>(TODOS_OS_DIAS);
        Collections.shuffle(copia);

        int qtdDias = 1 + new Random().nextInt(3); // 1 a 3
        List<Weekday> diasEscolhidos = copia.subList(0, qtdDias);
        createStudyGroup.setStudyGroupWeekdays(diasEscolhidos);

        // Modalidade aleatória
        createStudyGroup.setModality(ModalityEnum.REMOTE);

        // isPrivate false (ou você pode randomizar)
        createStudyGroup.setIsPrivate(false);

        return createStudyGroup;
    }
}
