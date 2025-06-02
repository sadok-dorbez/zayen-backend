package com.zayen.controllers;

import com.zayen.dto.request.ItemFilterCriteria;
import com.zayen.dto.request.ItemRequest;
import com.zayen.dto.response.CustomPageResponse;
import com.zayen.entities.Item;
import com.zayen.enumeration.ItemStatus;
import com.zayen.enumeration.TunisianCity;
import com.zayen.services.ItemService;
import com.zayen.services.impl.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class ItemController {
    private final ItemService itemService;
    private final EmailService emailService;

    @PostMapping("/add")
    public ResponseEntity<Item> addItem(@ModelAttribute ItemRequest itemRequest,
                                                @RequestParam("images") List<MultipartFile> images) throws MessagingException, IOException {
        Item addedItem = itemService.addItem(itemRequest, images);
        emailService.sendNewPubEmail(addedItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedItem);
    }

    @GetMapping("/by-seller/{sellerId}")
    public ResponseEntity<CustomPageResponse<Item>> getItemsBySeller(
            @PathVariable Long sellerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        CustomPageResponse<Item> items = itemService.getItemsBySeller(sellerId, page, size);
        return ResponseEntity.ok(items);
    }
    @GetMapping("/by-seller/{sellerId}/{status}")
    public ResponseEntity<CustomPageResponse<Item>> getItemsBySellerByStatus(
            @PathVariable Long sellerId,
            @PathVariable ItemStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        CustomPageResponse<Item> items = itemService.getItemsBySellerByStatus(sellerId, status, page, size);
        return ResponseEntity.ok(items);
    }


    @GetMapping("/by-status/{status}")
    public ResponseEntity<CustomPageResponse<Item>> getItemsByStatus(
            @PathVariable ItemStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        CustomPageResponse<Item> items = itemService.getItemsByStatus(status, page, size);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/by-status-and-city/{status}/{city}")
    public ResponseEntity<CustomPageResponse<Item>> getItemsByStatusAndCity(
            @PathVariable ItemStatus status,
            @PathVariable(required = false) String city,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        CustomPageResponse<Item> items = itemService.getItemsByStatusAndCity(status, city, page, size);
        return ResponseEntity.ok(items);
    }


    @PutMapping("/publish/{itemyId}")
    public ResponseEntity<Item> publishItem(@PathVariable Long itemId) throws MessagingException {
        Item publishedItem = itemService.publishItem(itemId);
        sendEmailToSeller(publishedItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(publishedItem);
    }

    private void sendEmailToSeller(Item publishedItem) throws MessagingException {
        String subject = "Votre article a été publiée avec succès";
        String message = "Votre article a été publiée avec succès. Félicitations!";
        String sellerEmail = publishedItem.getSeller().getEmail();

        // Appeler la méthode d'envoi d'e-mail de votre service EmailService
        emailService.sendEmail(sellerEmail, subject, message);
    }


    @GetMapping("/filter")
    public ResponseEntity<CustomPageResponse<Item>>  filterItems(
            @ModelAttribute ItemFilterCriteria filterCriteria,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        CustomPageResponse<Item> customResponse = itemService.filterItems(filterCriteria, pageable);
        return ResponseEntity.ok(customResponse);
    }

    @GetMapping("/byid/{id}")
    public ResponseEntity<Item> getItemById(
            @PathVariable Long id) {
        return ResponseEntity.ok(itemService.getItemById(id));
    }

    @GetMapping("/latestPublished")
    public List<Item> getLatestPublishedItems() {
        return itemService.getLatestPublishedItems();
    }
}

