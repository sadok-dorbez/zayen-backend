package com.zayen.controllers;

import com.zayen.dto.response.*;
import com.zayen.entities.Client;
import com.zayen.entities.Seller;


import com.zayen.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/clients")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CustomPageResponse<Client>> getAllClients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getAllClients(page, size));
    }
    @GetMapping("/Sellers")
    public ResponseEntity<CustomPageResponse<Seller>> getAllSellers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getAllSellers(page, size));
    }


    @GetMapping("/client/{clientId}")
    public ResponseEntity<ClientDetailsDTO> getClientById(@PathVariable Long clientId) {
            return ResponseEntity.ok(userService.getClientById(clientId));
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<SellerDetailsDTO> getSellerrById(@PathVariable Long sellerId) {
        return ResponseEntity.ok(userService.getSellerById(sellerId));
    }




    @PostMapping("/activate/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SuccessMessageResponse> activateAccount(@PathVariable Long userId) {
        userService.activateAccount(userId);
        return ResponseEntity.ok(new SuccessMessageResponse("Account activated successfully."));
    }

    @PostMapping("/deactivate/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SuccessMessageResponse> deactivateAccount(@PathVariable Long userId) {
        userService.deactivateAccount(userId);
        return ResponseEntity.ok(new SuccessMessageResponse("Account deactivated successfully."));
    }

}
