package com.ufftcc.boraestudar.entities;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = User.TABLE_NAME)
public class User {

    public static final String TABLE_NAME = "APPLICATION_USER";
    public static final String COLUMN_ID = "APUS_SQ_USER";
    public static final String COLUMN_NAME = "APUS_DS_NAME";
    public static final String COLUMN_EMAIL ="APUS_DS_EMAIL";
    public static final String COLUMN_PASSWORD = "APUS_DS_PASSWORD";
    public static final String COLUMN_IS_ENABLED = "APUS_IS_ENABLED";
    public static final String COLUMN_DISCORD_ID = "APUS_ID_DISCORD";

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

    @Column(name = COLUMN_IS_ENABLED, nullable = false)
    private Boolean isEnabled = false;

    @Column(name = COLUMN_DISCORD_ID, nullable = true)
    private Long discordId;

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

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    public Boolean isEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Long getDiscordId() { return discordId; }

    public void setDiscordId(Long discordId) { this.discordId = discordId; }

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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}