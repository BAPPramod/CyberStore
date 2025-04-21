package fi.haagahelia.cyberstore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import fi.haagahelia.cyberstore.domain.Cart;
import fi.haagahelia.cyberstore.domain.CartItem;
import fi.haagahelia.cyberstore.domain.CartRepository;
import fi.haagahelia.cyberstore.domain.Product;
import fi.haagahelia.cyberstore.domain.ProductRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public Cart getCartByUserId(String userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUserId(userId);
                    return cartRepository.save(cart);
                });
    }

    public CartItem addProductToCart(String userId, String productId, int quantity) {
        Cart cart = getCartByUserId(userId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem cartItem = CartItem.builder()
                .productId(product.getId())
                .productName(product.getName())
                .price(product.getPrice())
                .quantity(quantity)
                .imageUrl(product.getImageUrl())
                .build();

        cart.addItem(cartItem);
        cartRepository.save(cart);

        return cartItem;
    }

    public void updateCartItemQuantity(String userId, String productId, int quantity) {
        Cart cart = getCartByUserId(userId);
        cart.updateItemQuantity(productId, quantity);
        cartRepository.save(cart);
    }

    public void removeCartItem(String userId, String productId) {
        Cart cart = getCartByUserId(userId);
        cart.removeItem(productId);
        cartRepository.save(cart);
    }

    public void clearCart(String userId) {
        Optional<Cart> optionalCart = cartRepository.findByUserId(userId);
        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            cart.getItems().clear();
            cartRepository.save(cart);
        }
    }
}