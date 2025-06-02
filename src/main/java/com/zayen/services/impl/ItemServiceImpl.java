package com.zayen.services.impl;

import com.zayen.dto.request.ItemFilterCrit;
import com.zayen.dto.request.ItemFilterCriteria;
import com.zayen.dto.request.ItemRequest;
import com.zayen.dto.response.CustomPageResponse;
import com.zayen.dto.response.CustomPageable;
import com.zayen.entities.*;
import com.zayen.enumeration.ItemStatus;
import com.zayen.enumeration.TunisianCity;
import com.zayen.exceptions.BadRequestException;
import com.zayen.exceptions.ImageLimitExceededException;
import com.zayen.exceptions.NotFoundException;
import com.zayen.repositories.*;
import com.zayen.services.ItemService;
import com.zayen.specifications.ItemSpecifications;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final SellerRepository sellerRepository;
    private final ClientRepository clientRepository;

    @Value("${application.image.remoteUrl}")
    private String baseUrl;

    @Value("${application.image.path.remote}")
    private String imagePath;



    @Override
    public Item addItem(ItemRequest itemRequest, List<MultipartFile> images) {
        if (itemRequest.getSellerId() == null ) {
            throw new IllegalArgumentException("SellerId must be provided.");
        }

        Item item = new Item();
        item.setRef(generateUniqueReference());
        item.setTitle(itemRequest.getTitle());
        item.setPrice(itemRequest.getPrice());
        item.setCity(itemRequest.getCity());
        item.setDescription(itemRequest.getDescription());
        item.setQuantity(itemRequest.getQuantity());
        item.setSize(itemRequest.getSize());
        item.setColor(itemRequest.getColor());
        item.setCategory(itemRequest.getCategory());
        item.setOwnerName(itemRequest.getOwnerName());


        if (itemRequest.getSellerId() != null) {
            Seller seller = sellerRepository.findById(itemRequest.getSellerId())
                    .orElseThrow(() -> new NotFoundException("Seller not found with id: " + itemRequest.getSellerId()));
            item.setSeller(seller);
            item.setStatus(ItemStatus.PENDING_VALIDATION);
        }

        List<Image> imageList = new ArrayList<>();
        for (MultipartFile image : images) {
            Image imageEntity = new Image();
            imageEntity.setItem(item);
            imageEntity.setImagePath(saveImage(image, item.getRef()));
            imageEntity.setImagePath(baseUrl + imageEntity.getImagePath());
            imageList.add(imageEntity);
        }
        item.setImages(imageList);

        return itemRepository.save(item);
    }


    private String saveImage(MultipartFile image, String ref) {
        String imageName =ref + "_" + UUID.randomUUID()+ '.'+image.getContentType().substring(6);
        imagePath = "/home/ubuntu/images/" + imageName;
        try {
            Files.copy(image.getInputStream(), Paths.get(imagePath), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
        }
        return imageName;
    }


    private String generateUniqueReference() {
        int refLength = 10;
        StringBuilder refBuilder = new StringBuilder(refLength);

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();

        for (int i = 0; i < refLength; i++) {
            int randomIndex = random.nextInt(characters.length());
            refBuilder.append(characters.charAt(randomIndex));
        }

        return refBuilder.toString();
    }

    @Override
    public CustomPageResponse<Item> getItemsBySeller(Long sellerId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Item> items= itemRepository.findBySellerId(sellerId, pageRequest);
        return createCustomPageResponse(items);
    }

    @Override
    public CustomPageResponse<Item> getItemsBySellerByStatus(Long sellerId, ItemStatus status, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Item> items= itemRepository.findBySellerIdAndStatus(sellerId, status, pageRequest);
        return createCustomPageResponse(items);
    }

    @Override
    public CustomPageResponse<Item> getItemsByStatus(ItemStatus status, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Item> items=   itemRepository.findByStatusOrderByIdDesc(status, pageRequest);
        return createCustomPageResponse(items);
    }

    @Override
    public CustomPageResponse<Item> getItemsByStatusAndCity(ItemStatus status, String city, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Item> items= itemRepository.findByStatusAndCityOrderByIdDesc(status, city, pageRequest);
        return createCustomPageResponse(items);
    }

    @Override
    public CustomPageResponse<Item> getItemsByStatusAndCity(ItemStatus status, TunisianCity cityFilter, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Item> items=   itemRepository.findByStatusAndCityOrderByIdDesc(status,cityFilter, pageRequest);
        return createCustomPageResponse(items);
    }

    @Override
    public Item publishItem(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found with id: " + itemId));
        if (item.getStatus() == ItemStatus.PUBLISHED) {
            throw new BadRequestException("Item already published.");
        }
        item.setStatus(ItemStatus.PUBLISHED);


        return itemRepository.save(item);
    }

    @Override
    public CustomPageResponse<Item> filterItems(ItemFilterCriteria filterCriteria, Pageable pageable) {
        Specification<Item> spec = buildItemSpecification2(filterCriteria);
        Page<Item> filteredItemss = itemRepository.findAll(spec, pageable);
        return createCustomPageResponse(filteredItemss);
    }

    @Override
    public Item getItemById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item not found with id: " + id));
        return item;
    }

    @Override
    public List<Item> getLatestPublishedItems() {
        return itemRepository.findTop5ByStatusOrderByIdDesc(ItemStatus.PUBLISHED);
    }

    private Specification<Item> buildItemSpecification2(ItemFilterCriteria filterCriteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Always filter by status PUBLISHED
            predicates.add(ItemSpecifications.statusIs(ItemStatus.PUBLISHED).toPredicate(root, query, criteriaBuilder));

            if (filterCriteria.getItemCategory() != null) {
                predicates.add(ItemSpecifications.itemCategoryIs(filterCriteria.getItemCategory())
                        .toPredicate(root, query, criteriaBuilder));
            }

            if (filterCriteria.getItemSize() != null) {
                predicates.add(ItemSpecifications.itemSizeIs(filterCriteria.getItemSize())
                        .toPredicate(root, query, criteriaBuilder));
            }

            if (filterCriteria.getItemColor() != null) {
                predicates.add(ItemSpecifications.itemColorIs(filterCriteria.getItemColor())
                        .toPredicate(root, query, criteriaBuilder));
            }

            if (filterCriteria.getPrice() > 0) {
                predicates.add(ItemSpecifications.priceEqual(filterCriteria.getPrice())
                        .toPredicate(root, query, criteriaBuilder));
            } else {
                if (filterCriteria.getMinPrice() > 0) {
                    predicates.add(ItemSpecifications.priceGreaterThanOrEqual(filterCriteria.getMinPrice())
                            .toPredicate(root, query, criteriaBuilder));
                }

                if (filterCriteria.getMaxPrice() > 0) {
                    predicates.add(ItemSpecifications.priceLessThanOrEqual(filterCriteria.getMaxPrice())
                            .toPredicate(root, query, criteriaBuilder));
                }
            }

            if (filterCriteria.getCity() != null && !filterCriteria.getCity().isBlank()) {
                predicates.add(ItemSpecifications.cityIsLike(filterCriteria.getCity())
                        .toPredicate(root, query, criteriaBuilder));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }


    private Specification<Item> buildItemSpecification(ItemFilterCriteria filterCriteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filterCriteria.getCity() != null && !filterCriteria.getCity().isBlank()) {
                predicates.add(ItemSpecifications.cityIsLike(filterCriteria.getCity()).toPredicate(root, query, criteriaBuilder));
            }

            if (filterCriteria.getItemCategory() != null) {
                predicates.add(ItemSpecifications.itemCategoryIs(filterCriteria.getItemCategory()).toPredicate(root, query, criteriaBuilder));
            }

            if (filterCriteria.getMinPrice() >= 0) {
                predicates.add(ItemSpecifications.priceGreaterThanOrEqual(filterCriteria.getMinPrice()).toPredicate(root, query, criteriaBuilder));
            }

            if (filterCriteria.getMaxPrice() > 0) {
                predicates.add(ItemSpecifications.priceLessThanOrEqual(filterCriteria.getMaxPrice()).toPredicate(root, query, criteriaBuilder));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }


    private CustomPageResponse<Item> createCustomPageResponse(Page<Item> items) {
        CustomPageable customPageable = new CustomPageable(
                items.getPageable().getPageSize(),
                items.getPageable().getPageNumber(),
                items.getTotalElements(),
                items.getTotalPages()
        );
        return new CustomPageResponse<>(
                items.getContent(),
                customPageable
        );
    }




}
