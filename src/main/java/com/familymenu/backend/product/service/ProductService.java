package com.familymenu.backend.product.service;

import com.familymenu.backend.product.api.event.ProductDeletionRequested;

import com.familymenu.backend.product.api.ProductApi;
import com.familymenu.backend.product.api.dto.ProductView;
import com.familymenu.backend.product.domain.Product;
import com.familymenu.backend.product.persistence.ProductEntity;
import com.familymenu.backend.product.persistence.ProductMapper;
import com.familymenu.backend.product.persistence.ProductRepository;
import com.familymenu.backend.shared.IdGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import com.familymenu.backend.product.service.exception.ProductNotFoundException;
import com.familymenu.backend.product.service.exception.ProductAlreadyExistsException;
import org.springframework.stereotype.Service;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements ProductApi {

    private static final int MAX_PRODUCT_NAME_LENGTH = 255;

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final IdGenerator idGenerator;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public ProductView resolveByName(String name) {
        String normalizedName = normalizeProductName(name);
        Optional<ProductEntity> existingProduct = productRepository.findByNameIgnoreCase(normalizedName);

        if (existingProduct.isPresent()) {
            return productMapper.toView(existingProduct.get());
        }

        return saveNewProduct(normalizedName);
    }

    @Override
    public Optional<ProductView> findById(String id) {
        return productRepository.findById(id)
                .map(productMapper::toView);
    }

    @Override
    public List<ProductView> getByIds(Collection<String> ids) {
        return productRepository.findAllById(ids).stream()
                .map(productMapper::toView)
                .toList();
    }

    public ProductView getProduct(String id) {
        ProductEntity product = findProductOrThrow(id);
        return productMapper.toView(product);
    }

    public List<ProductView> listProducts(String query) {
        String searchQuery = "";
        if (query != null) {
            searchQuery = query.strip();
        }

        return productRepository.findByNameContainingIgnoreCaseOrderByNameAsc(searchQuery).stream()
                .map(productMapper::toView)
                .toList();
    }

    @Transactional
    public ProductView createProduct(String name) {
        String normalizedName = normalizeProductName(name);
        Optional<ProductEntity> existingProduct = productRepository.findByNameIgnoreCase(normalizedName);

        if (existingProduct.isPresent()) {
            throw new ProductAlreadyExistsException(normalizedName);
        }

        return saveNewProduct(normalizedName);
    }

    @Transactional
    public ProductView updateProduct(String id, String name) {
        ProductEntity product = findProductOrThrow(id);
        String normalizedName = normalizeProductName(name);

        validateProductNameAvailable(id, normalizedName);
        product.setName(normalizedName);

        ProductEntity savedProduct = productRepository.save(product);
        return productMapper.toView(savedProduct);
    }

    @Transactional
    public void deleteProduct(String id) {
        findProductOrThrow(id);
        eventPublisher.publishEvent(new ProductDeletionRequested(id));
        productRepository.deleteById(id);

        // Check foreign-key constraints before returning; products used in dishes cannot be deleted.
        productRepository.flush();
    }

    private ProductEntity findProductOrThrow(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    private ProductView saveNewProduct(String normalizedName) {
        Product product = new Product(idGenerator.generate(), normalizedName);
        ProductEntity entity = productMapper.toEntity(product);
        ProductEntity savedProduct = productRepository.save(entity);

        return productMapper.toView(savedProduct);
    }

    private void validateProductNameAvailable(String productId, String normalizedName) {
        Optional<ProductEntity> existingProduct = productRepository.findByNameIgnoreCase(normalizedName);
        if (existingProduct.isEmpty()) {
            return;
        }

        if (!existingProduct.get().getId().equals(productId)) {
            throw new ProductAlreadyExistsException(normalizedName);
        }
    }

    private String normalizeProductName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name is required");
        }

        String normalizedName = name.strip().replaceAll("\\s+", " ");
        if (normalizedName.length() > MAX_PRODUCT_NAME_LENGTH) {
            throw new IllegalArgumentException("Product name is too long");
        }

        return normalizedName;
    }
}
