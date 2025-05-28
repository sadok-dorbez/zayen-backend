package com.zayen.services;

import com.zayen.dto.request.AuthenticationRequest;
import com.zayen.dto.request.RegisterRequest;
import com.zayen.dto.request.VerifyOtpRequest;
import com.zayen.dto.response.AuthenticationResponse;
import jakarta.mail.MessagingException;


public interface AuthenticationService {


    String register(RegisterRequest request) throws MessagingException;
    AuthenticationResponse authenticate(AuthenticationRequest request);
    String verifyOtp(VerifyOtpRequest request);
}