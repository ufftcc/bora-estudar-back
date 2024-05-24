package com.ufftcc.boraestudar.entities;

import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = StudyGroup.TABLE_NAME)
public class StudyGroup {
    public static final String TABLE_NAME = "STUDY_GROUP";
    public static final String COLUMN_ID = "STGR_SQ_STUDY_GROUP";
    public static final String COLUMN_TITLE = "STGR_DS_TITLE";
    public static final String COLUMN_DESCRIPTION = "STGR_DS_DESCRIPTION";
    public static final String COLUMN_MEETING_TIME = "STGR_DT_MEETING_TIME";
    private static final String COLUMN_OWNER_ID = "STGR_USER_SQ_USER_OWNER";
    private static final String COLUMN_TUTOR = "STGR_USER_SQ_USER_TUTOR";
    private static final String COLUMN_MAX_STUDENTS = "STGR_QT_MAX_STUDENTS";
    private static final String COLUMN_VISIBILITY = "STGR_IS_PRIVATE";
    public static final String STUDY_GROUP = "studyGroup";
    public static final String COLUMN_WEEKDAYS = "STGR_WEEKDAY";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COLUMN_ID)
    private Long id;

    @Column(name = COLUMN_TITLE, nullable = false)
    private String title;

    @Column(name = COLUMN_DESCRIPTION)
    private String description;

    @Column(name = COLUMN_OWNER_ID, nullable = false)
    private Long ownerId;

    @ManyToOne
    @JoinColumn(name = COLUMN_TUTOR, referencedColumnName = User.COLUMN_ID)
    private User tutor;

    @ManyToOne
    @JoinColumn(name = Subject.COLUMN_ID, referencedColumnName = Subject.COLUMN_ID,
            nullable = false)
    private Subject subject;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = STUDY_GROUP)
    private List<StudyGroupUser> students = new ArrayList<>();

    @Column(name = COLUMN_MAX_STUDENTS, nullable = false)
    private Integer maxStudents;

    @Column(name = COLUMN_MEETING_TIME)
    private LocalTime meetingTime;

    @Column(name = COLUMN_VISIBILITY, nullable = false)
    private Boolean isPrivate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = STUDY_GROUP)
    private List<StudyGroupWeekday> studyGroupWeekdays;

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

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public User getTutor() {
        return tutor;
    }

    public void setTutor(User tutor) {
        this.tutor = tutor;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public List<StudyGroupUser> getStudents() {
        return students;
    }

    public void setStudents(List<StudyGroupUser> students) {
        this.students = students;
    }

    public Integer getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(Integer maxStudents) {
        this.maxStudents = maxStudents;
    }

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public void addStudent(StudyGroupUser student) {
        students.add(student);
    }

    public void removeStudent(StudyGroupUser student) {
        students.remove(student);
    }

    public Boolean hasStudentSlotsAvailable() {
        return students.size() < maxStudents;
    }

    public Boolean hasTutor() {
        return tutor != null;
    }

    public LocalTime getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(LocalTime meetingTime) {
        this.meetingTime = meetingTime;
    }

    public List<StudyGroupWeekday> getStudyGroupWeekdays() {
        return studyGroupWeekdays;
    }

    public void setStudyGroupWeekdays(List<StudyGroupWeekday> meetingDays) {
        this.studyGroupWeekdays = meetingDays;
    }

    public void addStudyGroupWeekdays(StudyGroupWeekday weekday) {
        studyGroupWeekdays.add(weekday);
    }

    public void removeStudyGroupWeekdays(StudyGroupWeekday weekday) {
        studyGroupWeekdays.remove(weekday);
    }

    public Boolean getPrivate() {
        return isPrivate;
    }

    public void setPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }

}