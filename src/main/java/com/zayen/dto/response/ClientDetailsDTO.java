package com.zayen.dto.response;

import lombok.Data;

@Data
public class ClientDetailsDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String phoneNumber;
    private long ItemsWithClient;
    private long itemsPublished;
    private ClientItemStatsDTO itemStats;
}


