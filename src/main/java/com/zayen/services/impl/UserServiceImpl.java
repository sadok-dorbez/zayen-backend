package com.zayen.services.impl;

import com.zayen.dto.response.*;
import com.zayen.entities.*;
import com.zayen.enumeration.ItemStatus;
import com.zayen.exceptions.NotFoundException;
import com.zayen.repositories.*;
import com.zayen.services.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final SellerRepository sellerRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;


    @Override
    public CustomPageResponse<Client> getAllClients(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Client> clients= clientRepository.findAll(pageRequest);
        CustomPageable customPageable = new CustomPageable(
                clients.getPageable().getPageSize(),
                clients.getPageable().getPageNumber(),
                clients.getTotalElements(),
                clients.getTotalPages()
        );
        return new CustomPageResponse<>(
                clients.getContent(),
                customPageable
        );
    }

    @Override
    public CustomPageResponse<Seller> getAllSellers(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Seller> sellers= sellerRepository.findAll(pageRequest);
        CustomPageable customPageable = new CustomPageable(
                sellers.getPageable().getPageSize(),
                sellers.getPageable().getPageNumber(),
                sellers.getTotalElements(),
                sellers.getTotalPages()
        );
        return new CustomPageResponse<>(
                sellers.getContent(),
                customPageable
        );
    }


    @Override
    public void activateAccount(Long userId) {
        logger.info("Trying to activate account for user with ID: : {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));
        user.setActive(true);
        userRepository.save(user);
        logger.info("Account activated successfully for user with ID: : {}", userId);
    }

    @Override
    public void deactivateAccount(Long userId) {
        logger.info("Trying to deactivate account for user with ID: : {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));
        user.setActive(false);
        userRepository.save(user);
        logger.info("Account deactivated successfully for user with ID: : {}", userId);
    }


    @Override
    public ClientDetailsDTO getClientById(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new NotFoundException("Client not found with ID: " + clientId));

        // Count only items bought by this client
        long itemsBought = itemRepository.countItemsByStatusAndClientId(ItemStatus.WITH_CLIENT, clientId);

        ClientDetailsDTO responseDTO = new ClientDetailsDTO();
        responseDTO.setId(client.getId());
        responseDTO.setFirstname(client.getFirstname());
        responseDTO.setLastname(client.getLastname());
        responseDTO.setEmail(client.getEmail());
        responseDTO.setPhoneNumber(client.getPhoneNumber());

        // Create stats DTO
        ClientItemStatsDTO itemStats = new ClientItemStatsDTO(itemsBought);
        responseDTO.setItemStats(itemStats); // Add this property in the ClientDetailsDTO

        return responseDTO;
    }


    @Override
    public SellerDetailsDTO getSellerById(Long sellerId) {
        Seller seller =  sellerRepository.findById(sellerId)
                .orElseThrow(() -> new NotFoundException("Seller not found with ID: " + sellerId));
        SellerDetailsDTO responseDTO = new SellerDetailsDTO();
        responseDTO.setId(seller.getId());
        responseDTO.setFirstname(seller.getFirstname());
        responseDTO.setLastname(seller.getLastname());
        responseDTO.setEmail(seller.getEmail());
        responseDTO.setAge(seller.getAge());
        responseDTO.setPhoneNumber(seller.getPhoneNumber());
        responseDTO.setNationalIdentityCard(seller.getNationalIdentityCard());
        return responseDTO;
    }


}
