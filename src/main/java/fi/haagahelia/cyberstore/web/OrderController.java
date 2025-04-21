package fi.haagahelia.cyberstore.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import fi.haagahelia.cyberstore.domain.Cart;
import fi.haagahelia.cyberstore.domain.Order;
import fi.haagahelia.cyberstore.service.CartService;
import fi.haagahelia.cyberstore.service.OrderService;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;

    @GetMapping("/orders")
    public String viewOrders(Model model, Authentication authentication) {
        model.addAttribute("orders", orderService.getOrdersByUserId(authentication.getName()));
        return "orders";
    }

    @PostMapping("/orders/create")
    public String createOrder(Authentication authentication) {
        orderService.createOrderFromCart(
                authentication.getName(),
                authentication.getName());
        return "redirect:/orders";
    }

    @GetMapping("/order-confirmation")
    public String orderConfirmation(Authentication authentication, Model model) {
        Cart cart = cartService.getCartByUserId(authentication.getName());
        model.addAttribute("cart", cart);
        return "order-confirmation";
    }

    @PostMapping("/orders/complete")
    @ResponseBody
    public ResponseEntity<?> completeOrder(@RequestParam String orderId) {
        orderService.updateOrderStatus(orderId, Order.OrderStatus.COMPLETED);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Order completed successfully");

        return ResponseEntity.ok(response);
    }
}
