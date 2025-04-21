package fi.haagahelia.cyberstore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import fi.haagahelia.cyberstore.domain.Cart;
import fi.haagahelia.cyberstore.domain.Order;
import fi.haagahelia.cyberstore.domain.OrderRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;

    public Order createOrderFromCart(String userId, String username) {
        Cart cart = cartService.getCartByUserId(userId);

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cannot create order from empty cart");
        }

        Order order = Order.builder()
                .userId(userId)
                .username(username)
                .items(cart.getItems())
                .totalAmount(cart.getTotalAmount())
                .status(Order.OrderStatus.PENDING.name())
                .createdAt(LocalDateTime.now())
                .build();

        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(userId);

        return savedOrder;
    }

    public List<Order> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }

    public Optional<Order> getOrderById(String orderId) {
        return orderRepository.findById(orderId);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order updateOrderStatus(String orderId, Order.OrderStatus status) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(status.name());
            return orderRepository.save(order);
        }
        throw new RuntimeException("Order not found");
    }
}
