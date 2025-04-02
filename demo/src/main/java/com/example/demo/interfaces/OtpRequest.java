package com.example.demo.interfaces;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OtpRequest {
    private String fullName;
    private String email;
    private String otp;
}