package com.ufftcc.boraestudar.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = EmailVerificationToken.TABLE_NAME)
public class EmailVerificationToken {

    public static final String TABLE_NAME = "EMAIL_VERIFICATION_TOKEN";
    public static final String COLUMN_ID = "EMVT_SQ_EMAIL_VERIFICATION_TOKEN";
    public static final String COLUMN_TOKEN = "EMVT_DS_TOKEN";
    public static final String COLUMN_VALIDITY = "EMVT_DT_VALIDITY";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COLUMN_ID)
    private Long id;

    @Column(name = COLUMN_TOKEN, nullable = false, unique = true)
    private String token;

    @Column(name = COLUMN_VALIDITY, nullable = false)
    private LocalDateTime validity;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = User.COLUMN_ID, nullable = false)
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getValidity() {
        return validity;
    }

    public void setValidity(LocalDateTime expiryDate) {
        this.validity = expiryDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmailVerificationToken that = (EmailVerificationToken) o;

        if (!id.equals(that.id)) return false;
        return (!token.equals(that.token));
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + token.hashCode();
        return result;
    }
}
