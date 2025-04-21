package fi.haagahelia.cyberstore.web;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import fi.haagahelia.cyberstore.domain.Product;
import fi.haagahelia.cyberstore.service.ProductService;
import fi.haagahelia.cyberstore.service.UserService;
import jakarta.validation.Valid;

@Controller
@RequiredArgsConstructor
public class ProductController {
    @Autowired
    private final ProductService productService;
    @Autowired
    private final UserService userService;

    @GetMapping("/products")
    public String showProducts(Model model, Authentication authentication) {
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("isAdmin", authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ADMIN")));
        return "products";
    }

    @GetMapping("/products/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "add-product";
    }

    @PostMapping("/products/add")
    public String addProduct(@Valid @ModelAttribute("product") Product product, BindingResult result) {
        if (result.hasErrors()) {
            return "add-product";
        }

        productService.saveProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/products/edit/{id}")
    public String showEditProductForm(@PathVariable String id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        model.addAttribute("product", product);
        return "add-product";
    }

    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }
}