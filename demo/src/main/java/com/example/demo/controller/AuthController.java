package com.example.demo.controller;

import com.example.demo.Jwt.JwtResponse;
import com.example.demo.Jwt.LoginRequest;
import com.example.demo.Jwt.MessageResponse;
import com.example.demo.repository.UserRepository;
import com.example.demo.Security.JwtUtils;
import com.example.demo.service.SmtpGmailSenderService;
import com.example.demo.model.User;
import com.example.demo.interfaces.OtpRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final ConcurrentHashMap<String, String> otpMap = new ConcurrentHashMap<>();

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private SmtpGmailSenderService emailService;

    /**
     * Authenticate user after OTP verification.
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), null));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername());

        Set<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority().replace("ROLE_", ""))
                .collect(Collectors.toSet());

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                roles));
    }

    /**
     * Send OTP to an existing user's email.
     */
    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody LoginRequest loginRequest) {
        if (!userRepository.existsByEmail(loginRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Please use an authorized email ID."));
        }
        String otp = generateOtp();
        otpMap.put(loginRequest.getEmail(), otp);
        emailService.sendEmail(loginRequest.getEmail(), "OKR-Verification OTP", "Your OTP is: " + otp);
        return ResponseEntity.ok(Map.of("success", true, "message", "OTP sent to your email."));
    }

    /**
     * Verify OTP and allow user login.
     */
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOTP(@Valid @RequestBody OtpRequest otpRequest) {
        String storedOtp = otpMap.get(otpRequest.getEmail());
        System.out.println("Stored OTP: " + storedOtp + " | Received OTP: " + otpRequest.getOtp());
        if (storedOtp == null || !storedOtp.equals(otpRequest.getOtp())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid OTP!"));
        }
        User user = userRepository.findByEmail(otpRequest.getEmail());
        if (user == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Unauthorized email ID."));
        }
        otpMap.remove(otpRequest.getEmail()); // OTP is no longer valid
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);
        return ResponseEntity.ok(new JwtResponse(jwt, user.getUserId(), user.getUsername(), user.getEmail(), user.getRoles()));
    }
    //     i'm generating otp here
    private String generateOtp() {
        return String.format("%06d", (int) (Math.random() * 1000000));
    }
}
