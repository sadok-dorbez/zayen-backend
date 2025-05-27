package com.zayen.dto.response;

import lombok.Data;

@Data
public class SellerDetailsDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String phoneNumber;
    private int age;
    private String nationalIdentityCard;
}


