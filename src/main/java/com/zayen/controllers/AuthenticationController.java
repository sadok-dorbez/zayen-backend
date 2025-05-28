package com.zayen.controllers;

import com.zayen.dto.request.AuthenticationRequest;
import com.zayen.dto.request.RegisterRequest;
import com.zayen.dto.request.VerifyOtpRequest;
import com.zayen.dto.response.AuthenticationResponse;
import com.zayen.dto.response.SuccessMessageResponse;
import com.zayen.services.AuthenticationService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<SuccessMessageResponse> register(
            @RequestBody RegisterRequest request
    ) throws MessagingException {
        return ResponseEntity.ok(new SuccessMessageResponse(service.register(request)));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<SuccessMessageResponse> verifyOTP(
            @RequestBody VerifyOtpRequest request
    ) {
        return ResponseEntity.ok(new SuccessMessageResponse(service.verifyOtp(request)));
    }
}
