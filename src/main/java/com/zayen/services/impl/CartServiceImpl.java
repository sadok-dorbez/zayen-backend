package com.zayen.services.impl;

import com.zayen.dto.request.CartItemRequest;
import com.zayen.dto.response.CartItemResponse;
import com.zayen.entities.Cart;
import com.zayen.entities.CartItem;
import com.zayen.entities.Client;
import com.zayen.entities.Item;
import com.zayen.exceptions.NotFoundException;
import com.zayen.repositories.CartItemRepository;
import com.zayen.repositories.CartRepository;
import com.zayen.repositories.ClientRepository;
import com.zayen.repositories.ItemRepository;
import com.zayen.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final ClientRepository clientRepository;
    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;

    @Override
    public CartItemResponse addToCart(Long clientId, Long itemId, int quantity) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new NotFoundException("Client not found with id: " + clientId));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found with id: " + itemId));

        // Get or create the client's cart
        Cart cart = client.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setClient(client);
            cart.setItems(new ArrayList<>());
            cart.setTotalPrice(0.0); // Initial total
            cart = cartRepository.save(cart);
            client.setCart(cart); // Optional if bidirectional
            clientRepository.save(client); // Persist the link
        }

        // Create the new cart item
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setQuantity(quantity);

        cartItem = cartItemRepository.save(cartItem);

        // Optionally update the cart total
        double itemTotal = item.getPrice() * quantity;
        cart.setTotalPrice(cart.getTotalPrice() + itemTotal);
        cartRepository.save(cart);

        return new CartItemResponse(cartItem);
    }

    @Override
    public void removeFromCart(Long cartItemId) {
        if (!cartItemRepository.existsById(cartItemId)) {
            throw new NotFoundException("Cart item not found with id: " + cartItemId);
        }
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    public CartItemResponse updateQuantity(Long cartItemId, int newQty) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new NotFoundException("Cart item not found with id: " + cartItemId));
        cartItem.setQuantity(newQty);
        return new CartItemResponse(cartItemRepository.save(cartItem));
    }

    @Override
    public List<CartItemResponse> getCartByClient(Long clientId) {
        clientRepository.findById(clientId)
                .orElseThrow(() -> new NotFoundException("Client not found with id: " + clientId));

        return cartItemRepository.findByCart_Client_Id(clientId).stream()
                .map(CartItemResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public void clearCart(Long clientId) {
        clientRepository.findById(clientId)
                .orElseThrow(() -> new NotFoundException("Client not found with id: " + clientId));

        cartItemRepository.deleteAllByCart_Client_Id(clientId);
    }

}