package com.ufftcc.boraestudar.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = StudyGroupUser.TABLE_NAME,
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_STUDY_GROUP_USER",
                        columnNames = {
                                User.COLUMN_ID,
                                StudyGroup.COLUMN_ID})
        })
public class StudyGroupUser {

    public static final String TABLE_NAME = "STUDY_GROUP_USER";
    public static final String COLUMN_ID = "STGU_SQ_STUDY_GROUP_USER";
    public static final String CONSTRAINT_STUDY_GROUP_STUDY_GROUP_USER = "FK_STUDY_GROUP_STUDY_GROUP_USER";
    public static final String CONSTRAINT_APPLICATION_USER_STUDY_GROUP_USER = "FK_APPLICATION_USER_STUDY_GROUP_USER";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COLUMN_ID)
    private Long id;

    @ManyToOne
    @JoinColumn(name = User.COLUMN_ID,
            foreignKey = @ForeignKey(name = CONSTRAINT_APPLICATION_USER_STUDY_GROUP_USER),
            nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = StudyGroup.COLUMN_ID,
            foreignKey = @ForeignKey(name = CONSTRAINT_STUDY_GROUP_STUDY_GROUP_USER),
            nullable = false)
    private StudyGroup studyGroup;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public StudyGroup getStudyGroup() {
        return studyGroup;
    }

    public void setStudyGroup(StudyGroup studyGroup) {
        this.studyGroup = studyGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudyGroupUser that = (StudyGroupUser) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(user, that.user)) return false;
        return Objects.equals(studyGroup, that.studyGroup);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (studyGroup != null ? studyGroup.hashCode() : 0);
        return result;
    }
}