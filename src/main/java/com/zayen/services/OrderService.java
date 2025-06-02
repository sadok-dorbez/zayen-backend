package com.zayen.services;

import com.zayen.enumeration.PaymentMethod;
import com.zayen.entities.Order;

public interface OrderService {
    Order placeOrder(Long clientId, PaymentMethod paymentMethod);
}
