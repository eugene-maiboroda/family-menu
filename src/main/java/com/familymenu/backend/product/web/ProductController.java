package com.familymenu.backend.product.web;

import com.familymenu.backend.product.service.ProductService;
import com.familymenu.backend.product.api.dto.ProductView;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    public record ProductRequest(@NotBlank @Size(max = 255) String name) {
    }

    @GetMapping
    public List<ProductView> listProducts(@RequestParam(required = false) String query) {
        return productService.listProducts(query);
    }

    @GetMapping("/{id}")
    public ProductView getProduct(@PathVariable String id) {
        return productService.getProduct(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductView createProduct(@Valid @RequestBody ProductRequest request) {
        return productService.createProduct(request.name());
    }

    @PutMapping("/{id}")
    public ProductView updateProduct(
            @PathVariable String id,
            @Valid @RequestBody ProductRequest request
    ) {
        return productService.updateProduct(id, request.name());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
    }
}
