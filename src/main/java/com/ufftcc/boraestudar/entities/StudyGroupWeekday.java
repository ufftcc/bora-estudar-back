package com.ufftcc.boraestudar.entities;

import jakarta.persistence.*;

@Entity
@Table(name = StudyGroupWeekday.TABLE_NAME,
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_STUDY_GROUP_WEEKDAY",
                        columnNames = {
                                StudyGroup.COLUMN_ID,
                                Weekday.COLUMN_ID})
        })
public class StudyGroupWeekday {

    public static final String TABLE_NAME = "STUDY_GROUP_WEEKDAY";
    public static final String COLUMN_ID = "STWD_SQ_STUDY_GROUP_WEEKDAY";
    public static final String CONSTRAINT_STUDY_GROUP_STUDY_GROUP_WEEKDAY = "FK_STUDY_GROUP_STUDY_GROUP_WEEKDAY";
    public static final String CONSTRAINT_WEEKDAY_STUDY_GROUP_WEEKDAY = "FK_WEEKDAY_STUDY_GROUP_WEEKDAY";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COLUMN_ID)
    private Long id;

    @ManyToOne
    @JoinColumn(name = StudyGroup.COLUMN_ID,
            foreignKey = @ForeignKey(name = CONSTRAINT_STUDY_GROUP_STUDY_GROUP_WEEKDAY),
            nullable = false)
    private StudyGroup studyGroup;

    @ManyToOne
    @JoinColumn(name = Weekday.COLUMN_ID,
            foreignKey = @ForeignKey(name = CONSTRAINT_WEEKDAY_STUDY_GROUP_WEEKDAY),
            nullable = false)
    private Weekday weekday;

    public StudyGroupWeekday() {
    }

    public StudyGroupWeekday(StudyGroup studyGroup, Weekday weekday) {
        this.studyGroup = studyGroup;
        this.weekday = weekday;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StudyGroup getStudyGroup() {
        return studyGroup;
    }

    public void setStudyGroup(StudyGroup studyGroup) {
        this.studyGroup = studyGroup;
    }

    public Weekday getWeekday() {
        return weekday;
    }

    public void setWeekday(Weekday weekDay) {
        this.weekday = weekDay;
    }

}
