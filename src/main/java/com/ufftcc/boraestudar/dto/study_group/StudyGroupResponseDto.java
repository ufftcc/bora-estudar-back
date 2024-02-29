package com.ufftcc.boraestudar.dto.study_group;

import com.ufftcc.boraestudar.dto.subject.SubjectResponseDto;
import com.ufftcc.boraestudar.dto.user.UserResponseBasicDto;

import java.util.List;

public class StudyGroupResponseDto {
    private Long id;

    private String title;

    private String description;

    private Long ownerId;

    private UserResponseBasicDto tutor;

    private SubjectResponseDto subject;

    private List<UserResponseBasicDto> students;

    private Integer maxStudents;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public UserResponseBasicDto getTutor() {
        return tutor;
    }

    public void setTutor(UserResponseBasicDto tutor) {
        this.tutor = tutor;
    }

    public SubjectResponseDto getSubject() {
        return subject;
    }

    public void setSubject(SubjectResponseDto subject) {
        this.subject = subject;
    }

    public List<UserResponseBasicDto> getStudents() {
        return students;
    }

    public void setStudents(List<UserResponseBasicDto> students) {
        this.students = students;
    }

    public Integer getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(Integer maxStudents) {
        this.maxStudents = maxStudents;
    }
}
