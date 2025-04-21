package fi.haagahelia.cyberstore.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import fi.haagahelia.cyberstore.domain.Cart;
import fi.haagahelia.cyberstore.service.CartService;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/cart")
    public String viewCart(Model model, Authentication authentication) {
        Cart cart = cartService.getCartByUserId(authentication.getName());
        model.addAttribute("cart", cart);
        return "cart";
    }

    @PostMapping("/cart/add/{productId}")
    @ResponseBody
    public ResponseEntity<?> addToCart(@PathVariable String productId,
            @RequestParam(defaultValue = "1") int quantity,
            Authentication authentication) {
        cartService.addProductToCart(authentication.getName(), productId, quantity);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Product added to cart successfully");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/cart/update/{productId}")
    @ResponseBody
    public ResponseEntity<?> updateCartQuantity(@PathVariable String productId,
            @RequestParam int quantity,
            Authentication authentication) {
        if (quantity <= 0) {
            cartService.removeCartItem(authentication.getName(), productId);
        } else {
            cartService.updateCartItemQuantity(authentication.getName(), productId, quantity);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Cart updated successfully");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/cart/remove/{productId}")
    @ResponseBody
    public ResponseEntity<?> removeFromCart(@PathVariable String productId,
            Authentication authentication) {
        cartService.removeCartItem(authentication.getName(), productId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Item removed from cart");

        return ResponseEntity.ok(response);
    }
}
