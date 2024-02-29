package com.ufftcc.boraestudar.entities;

import jakarta.persistence.*;

@Entity
@Table(name = User.TABLE_NAME)
public class User {

    public static final String TABLE_NAME = "APPLICATION_USER";
    public static final String COLUMN_ID = "APUS_SQ_USER";
    public static final String COLUMN_NAME = "APUS_DS_NAME";
    public static final String COLUMN_EMAIL ="APUS_DS_EMAIL";
    public static final String COLUMN_PASSWORD = "APUS_DS_PASSWORD";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COLUMN_ID, unique = true)
    private Long id;

    @Column(name = COLUMN_NAME, nullable = false)
    private String name;

    @Column(name = COLUMN_EMAIL, nullable = false, unique = true)
    private String email;

    @Column(name = COLUMN_PASSWORD, nullable = false)
    private String password;

    public User() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!id.equals(user.id)) return false;
        return email.equals(user.email);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + email.hashCode();
        return result;
    }
}