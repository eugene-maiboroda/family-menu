package com.familymenu.backend.dish.service;

import com.familymenu.backend.dish.api.event.DishDeletionRequested;

import com.familymenu.backend.dish.api.DishApi;
import com.familymenu.backend.dish.api.dto.DishIngredientView;
import com.familymenu.backend.dish.api.dto.DishView;
import com.familymenu.backend.dish.api.dto.RecipeStepView;
import com.familymenu.backend.dish.domain.Dish;
import com.familymenu.backend.dish.domain.DishIngredient;
import com.familymenu.backend.dish.domain.ProductReference;
import com.familymenu.backend.dish.persistence.DishPersistenceService;
import com.familymenu.backend.product.api.ProductApi;
import com.familymenu.backend.product.api.dto.ProductView;
import com.familymenu.backend.shared.IdGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DishService implements DishApi {

    private final DishPersistenceService dishPersistence;
    private final ProductApi productApi;
    private final IdGenerator idGenerator;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Dish createDish(Dish dish) {
        List<DishIngredient> resolvedIngredients = resolveIngredients(dish.getIngredients());

        Dish dishForSave = dish.toBuilder()
                .id(idGenerator.generate())
                .recipeSteps(dish.getRecipeSteps() == null ? List.of() : dish.getRecipeSteps())
                .ingredients(resolvedIngredients)
                .build();

        dishPersistence.saveDish(dishForSave);
        return dishForSave;
    }

    @Transactional
    public Dish updateDish(Dish changes) {

        Dish existing = findOrThrow(changes.getId());

        Dish updated = existing.toBuilder()
                .name(changes.getName())
                .category(changes.getCategory())
                .recipeSteps(changes.getRecipeSteps() == null ? List.of() : changes.getRecipeSteps())
                .ingredients(resolveIngredients(changes.getIngredients()))
                .build();

        dishPersistence.saveDish(updated);

        return updated;
    }

    @Transactional
    public void deleteDish(String id) {
        findOrThrow(id);
        eventPublisher.publishEvent(new DishDeletionRequested(id));
        dishPersistence.deleteDishById(id);
    }

    public List<Dish> listDishes() {
        return dishPersistence.findDishes().stream()
                .map(this::resolveProductReferences)
                .toList();
    }

    public Dish getDish(String id) {
        return findOrThrow(id);
    }

    @Override
    public List<DishView> getByIds(Collection<String> ids) {
        return ids.stream()
                .map(this::findById)
                .flatMap(Optional::stream)
                .map(this::toView)
                .toList();
    }

    private List<DishIngredient> resolveIngredients(List<DishIngredient> ingredients) {
        if (ingredients == null || ingredients.isEmpty()) {
            throw new IllegalArgumentException("Dish needs ingredients");
        }

        List<DishIngredient> resolvedIngredients = ingredients.stream()
                .map(this::resolveIngredient)
                .toList();

        validateUniqueProducts(resolvedIngredients);
        return resolvedIngredients;
    }

    private void validateUniqueProducts(List<DishIngredient> ingredients) {
        Set<String> productIds = new HashSet<>();

        for (DishIngredient ingredient : ingredients) {
            String productId = ingredient.product().id();
            if (!productIds.add(productId)) {
                throw new IllegalArgumentException("Product occurs more than once in the dish: " + productId);
            }
        }
    }

    private DishIngredient resolveIngredient(DishIngredient ingredient) {
        ProductView product = resolveProduct(ingredient.product());
        ProductReference productReference = new ProductReference(product.id(), product.name());

        return ingredient.toBuilder()
                .id(idGenerator.generate())
                .product(productReference)
                .build();
    }

    private ProductView resolveProduct(ProductReference reference) {
        String productId = reference.id();
        if (productId == null) {
            return productApi.resolveByName(reference.name());
        }

        return productApi.findById(productId)
                .orElseThrow(() -> new DishProductNotFoundException(productId));
    }

    private Dish findOrThrow(String id) {
        return findById(id)
                .orElseThrow(() -> new DishNotFoundException(id + " not found"));
    }

    private Optional<Dish> findById(String id) {
        return dishPersistence.findDishById(id)
                .map(this::resolveProductReferences);
    }

    private Dish resolveProductReferences(Dish dish) {
        List<String> productIds = dish.getIngredients().stream()
                .map(ingredient -> ingredient.product().id())
                .distinct()
                .toList();

        Map<String, ProductReference> productsById = productApi.getByIds(productIds).stream()
                .collect(Collectors.toMap(
                        ProductView::id,
                        product -> new ProductReference(product.id(), product.name())
                ));

        List<DishIngredient> resolvedIngredients = dish.getIngredients().stream()
                .map(ingredient -> ingredient.toBuilder()
                        .product(requireProductReference(productsById, ingredient.product().id()))
                        .build())
                .toList();

        return dish.toBuilder()
                .ingredients(resolvedIngredients)
                .build();
    }

    private ProductReference requireProductReference(Map<String, ProductReference> productsById, String productId) {
        ProductReference product = productsById.get(productId);
        if (product == null) {
            throw new IllegalStateException(
                    "Product %s referenced by dish ingredient was not found".formatted(productId)
            );
        }
        return product;
    }

    private DishView toView(Dish dish) {
        List<DishIngredientView> allIngredients = dish.getIngredients().stream()
                .map(this::toIngredientView)
                .toList();

        List<RecipeStepView> allSteps = dish.getRecipeSteps().stream()
                .map(step -> new RecipeStepView(step.description(), step.imageUrl()))
                .toList();

        return new DishView(dish.getId(), dish.getName(), dish.getCategory().name(), allIngredients, allSteps);
    }

    private DishIngredientView toIngredientView(DishIngredient ingredient) {
        return new DishIngredientView(
                ingredient.product().id(),
                ingredient.product().name(),
                ingredient.amount(),
                ingredient.unit().name()
        );
    }
}
