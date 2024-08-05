package com.ufftcc.boraestudar.entities;

import jakarta.persistence.*;

@Entity
@Table(name = Weekday.TABLE_NAME)
public class Weekday {

    public static final String TABLE_NAME = "WEEKDAY";
    public static final String COLUMN_ID = "WEEK_SQ_WEEKDAY";
    public static final String COLUMN_NAME = "WEEK_DS_NAME";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COLUMN_ID, unique = true)
    private Long id;

    @Column(name = COLUMN_NAME, nullable = false)
    private String name;

    public Weekday() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

        Weekday weekday = (Weekday) o;

        return id.equals(weekday.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
