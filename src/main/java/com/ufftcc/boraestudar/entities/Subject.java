package com.ufftcc.boraestudar.entities;

import jakarta.persistence.*;

@Entity
@Table(name = Subject.TABLE_NAME)
public class Subject {

    public static final String TABLE_NAME = "SUBJECT";
    public static final String COLUMN_ID = "SUBJ_SQ_SUBJECT";
    public static final String COLUMN_CODE = "SUBJ_CD_CODE";
    public static final String COLUMN_NAME = "SUBJ_DS_NAME";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COLUMN_ID, unique = true)
    private Long id;

    @Column(name = COLUMN_CODE, nullable = false, unique = true)
    private String code;

    @Column(name = COLUMN_NAME, nullable = false)
    private String name;

    public Subject() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Subject subject = (Subject) o;

        if (!id.equals(subject.id)) return false;
        return code.equals(subject.code);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + code.hashCode();
        return result;
    }
}