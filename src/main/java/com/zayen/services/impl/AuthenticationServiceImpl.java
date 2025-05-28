package com.zayen.services.impl;

import com.zayen.dto.request.AuthenticationRequest;
import com.zayen.dto.request.RegisterRequest;
import com.zayen.dto.request.VerifyOtpRequest;
import com.zayen.dto.response.AuthenticationResponse;
import com.zayen.entities.*;
import com.zayen.exceptions.AccountDisabledException;
import com.zayen.exceptions.EmailAlreadyExistsException;
import com.zayen.exceptions.InvalidOtpException;
import com.zayen.exceptions.NotFoundException;
import com.zayen.repositories.*;
import com.zayen.security.JwtService;
import com.zayen.services.AuthenticationService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    @Override
    public String register(RegisterRequest request) throws MessagingException {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        // Générer un code OTP
        String otpCode = generateOTP();
        User user;
        switch (request.getRole()) {
            case SELLER:
                user = new Seller(
                        request.getFirstname(),
                        request.getLastname(),
                        request.getEmail(),
                        passwordEncoder.encode(request.getPassword()),
                        request.getRole(),
                        request.getPhoneNumber(),
                        request.getAge(),
                        request.getNationalIdentityCard()
                );
                user.setActive(false); // Marquer l'utilisateur comme inactif jusqu'à la vérification de l'e-mail
                user.setOtpCode(otpCode); // Stocker le code OTP dans l'entité User
                sellerRepository.save((Seller) user);
                emailService.sendVerificationEmail(user.getEmail(), otpCode);
                break;

            case CLIENT:
                user = new Client(
                        request.getFirstname(),
                        request.getLastname(),
                        request.getEmail(),
                        passwordEncoder.encode(request.getPassword()),
                        request.getRole(),
                        request.getPhoneNumber()
                );
                user.setActive(true);
                clientRepository.save((Client) user);
                break;

            default:
                throw new IllegalArgumentException("Invalid user role.");
        }

        return "Registration successful. Check your email for the verification code.";
    }


    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(createAuthenticationToken(request));
        } catch (BadCredentialsException e) {
            throw new NotFoundException("Invalid email or password, please try again");
        }

        User user = findUserByEmail(request.getEmail());
        if (!user.isActive()) {
            throw new AccountDisabledException("Account is disabled. Please contact support.");
        }
        String jwtToken = jwtService.generateToken(user);


        return createAuthenticationResponse(jwtToken, user.getRole().name(), user.getId());
    }

    @Override
    public String verifyOtp(VerifyOtpRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("Utilisateur introuvable"));

        if (request.getOtpCode().equals(user.getOtpCode())) {
            user.setActive(true);
            userRepository.save(user);
            return "La vérification de l'e-mail a réussi. Vous pouvez maintenant vous connecter.";
        }else {
            throw new InvalidOtpException("Code invalide. Veuillez réessayer.");
        }
    }
    private UsernamePasswordAuthenticationToken createAuthenticationToken(AuthenticationRequest request) {
        return new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));
    }

    private AuthenticationResponse createAuthenticationResponse(String jwtToken, String role, Long userId) {
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .role(role)
                .userId(userId)
                .build();
    }

    private String generateOTP() {
        // Générer un code OTP aléatoire de 4 chiffres
        Random random = new Random();
        int otp = 1000 + random.nextInt(9000);

        return String.valueOf(otp);
    }
}
