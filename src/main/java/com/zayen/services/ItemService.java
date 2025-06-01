package com.zayen.services;

import com.zayen.dto.request.ItemFilterCrit;
import com.zayen.dto.request.ItemRequest;
import com.zayen.dto.response.CustomPageResponse;
import com.zayen.entities.Item;
import com.zayen.enumeration.ItemStatus;
import com.zayen.enumeration.TunisianCity;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ItemService {

    Item addItem(ItemRequest itemRequest, List<MultipartFile> images);
    CustomPageResponse<Item> getItemsBySeller(Long sellerId, int page, int size);
    CustomPageResponse<Item> getItemsBySellerByStatus(Long sellerId, ItemStatus status, int page, int size);
    CustomPageResponse<Item> getItemsByStatusAndCity(ItemStatus status, TunisianCity cityFilter, int page, int size);
    CustomPageResponse<Item> getItemsByStatusAndCity(ItemStatus status, String city, int page, int size);
    CustomPageResponse<Item> getItemsByStatus(ItemStatus status, int page, int size);
    Item publishItem(Long propertyId);
    CustomPageResponse<Item> filterItems(ItemFilterCrit filterCriteria, Pageable pageable);

    Item getItemById(Long id);
    List<Item> getLatestPublishedItems();

}
