package com.zayen.services;

import com.zayen.dto.response.ClientDetailsDTO;
import com.zayen.dto.response.CustomPageResponse;
import com.zayen.dto.response.SellerDetailsDTO;
import com.zayen.entities.Client;
import com.zayen.entities.Seller;

public interface UserService {

    CustomPageResponse<Client> getAllClients(int page, int size);
    CustomPageResponse<Seller> getAllSellers(int page, int size);

    void activateAccount(Long userId);
    void deactivateAccount(Long userId);

    ClientDetailsDTO getClientById(Long clientId);
    SellerDetailsDTO getSellerById(Long sellerId);
}
