package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Setter;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Entity
@Table(name = "user_verification")
public class UserVerification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "verification_id", nullable = false)
    private Long verificationId;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Column(name = "otp")
    private String otp;


    @Column(name = "otp_expiry")
    private LocalDateTime otpExpiry;

    @Column(name = "verification_code")
    private String verificationCode;

    @Column(name = "verification_expiry")
    private LocalDateTime verificationExpiry;

    public CharSequence getOtpExpiry() {
        return otpExpiry != null ? otpExpiry.toString() : "DummyString";
    }
}

