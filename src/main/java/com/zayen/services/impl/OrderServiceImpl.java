package com.zayen.services.impl;

import com.zayen.entities.*;
import com.zayen.enumeration.OrderStatus;
import com.zayen.enumeration.PaymentMethod;
import com.zayen.exceptions.BadRequestException;
import com.zayen.exceptions.NotFoundException;
import com.zayen.repositories.*;
import com.zayen.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final ClientRepository clientRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    @Transactional
    public Order placeOrder(Long clientId, PaymentMethod paymentMethod) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new NotFoundException("Client not found with id: " + clientId));

        Cart cart = client.getCart();
        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new BadRequestException("Cart is empty. Cannot place order.");
        }

        // Create Order
        Order order = new Order();
        order.setClient(client);
        order.setOrderDate(LocalDateTime.now());
        order.setPaymentMethod(paymentMethod);
        order.setStatus(OrderStatus.PENDING);

        List<OrderItem> orderItems = cart.getItems().stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setItem(cartItem.getItem());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtPurchase(cartItem.getItem().getPrice());
            return orderItem;
        }).collect(Collectors.toList());

        order.setItems(orderItems);
        order.setTotalPrice(orderItems.stream()
                .mapToDouble(item -> item.getQuantity() * item.getPriceAtPurchase())
                .sum());

        // Save order and items
        Order savedOrder = orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);

        // Clear cart
        cartItemRepository.deleteAllByCart_Id(cart.getId());
        cart.setTotalPrice(0.0);
        cartRepository.save(cart);

        return savedOrder;
    }
}
