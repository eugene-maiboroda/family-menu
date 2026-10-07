package com.familymenu.backend.shopping.service;

import com.familymenu.backend.dish.api.DishApi;
import com.familymenu.backend.product.api.ProductApi;
import com.familymenu.backend.shopping.service.exception.ShoppingDishNotFoundException;
import com.familymenu.backend.shopping.service.exception.ShoppingProductNotFoundException;
import com.familymenu.backend.menu.api.MenuApi;
import com.familymenu.backend.menu.api.dto.MenuView;
import com.familymenu.backend.shopping.domain.ShoppingListItem;
import com.familymenu.backend.shopping.domain.Unit;
import com.familymenu.backend.shopping.domain.ManualProductPurchase;
import com.familymenu.backend.shopping.persistence.ShoppingListEntity;
import com.familymenu.backend.shopping.persistence.ShoppingListRepository;
import com.familymenu.backend.shopping.service.dto.AdditionalProduct;
import com.familymenu.backend.shopping.service.dto.SavedShoppingListView;
import com.familymenu.backend.shopping.service.dto.SavedShoppingListView.ManualPurchaseView;
import com.familymenu.backend.shopping.service.exception.InboxModificationException;
import com.familymenu.backend.shopping.service.exception.ShoppingListNotFoundException;
import com.familymenu.backend.shopping.service.exception.ShoppingMenuNotFoundException;
import com.familymenu.backend.shared.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SavedShoppingListService {

    public static final String INBOX_ID = "inbox";
    private static final int MAX_LIST_NAME_LENGTH = 255;

    private final ShoppingListRepository shoppingListRepository;
    private final ShoppingListService shoppingListService;
    private final MenuApi menuApi;
    private final DishApi dishApi;
    private final ProductApi productApi;
    private final IdGenerator idGenerator;

    @Transactional(readOnly = true)
    public SavedShoppingListView getShoppingList(String id) {
        ShoppingListEntity shoppingList = findShoppingListOrThrow(id);
        return toShoppingListView(shoppingList);
    }

    @Transactional(readOnly = true)
    public List<SavedShoppingListView> listShoppingLists() {
        return shoppingListRepository.findAllByOrderByInboxDescNameAsc().stream()
                .map(this::toShoppingListView)
                .toList();
    }

    public SavedShoppingListView createShoppingList(String name) {
        ShoppingListEntity shoppingList = new ShoppingListEntity();
        shoppingList.setId(idGenerator.generate());
        shoppingList.setName(normalizeListName(name));
        return saveShoppingList(shoppingList);
    }

    public SavedShoppingListView renameShoppingList(String id, String name) {
        ShoppingListEntity shoppingList = findShoppingListOrThrow(id);
        validateCustomList(shoppingList);
        shoppingList.setName(normalizeListName(name));
        return saveShoppingList(shoppingList);
    }

    public void deleteShoppingList(String id) {
        ShoppingListEntity shoppingList = findShoppingListOrThrow(id);
        validateCustomList(shoppingList);
        shoppingListRepository.delete(shoppingList);
    }

    public SavedShoppingListView clearShoppingList(String id) {
        ShoppingListEntity shoppingList = findShoppingListOrThrow(id);
        shoppingList.getMenuIds().clear();
        shoppingList.getDishIds().clear();
        shoppingList.getManualProducts().clear();
        return saveShoppingList(shoppingList);
    }

    public SavedShoppingListView addMenuToShoppingList(String id, String menuId) {
        ShoppingListEntity shoppingList = findShoppingListOrThrow(id);
        findMenuOrThrow(menuId);
        if (!shoppingList.getMenuIds().contains(menuId)) {
            shoppingList.getMenuIds().add(menuId);
        }
        return saveShoppingList(shoppingList);
    }

    public SavedShoppingListView removeMenuFromShoppingList(String id, String menuId) {
        ShoppingListEntity shoppingList = findShoppingListOrThrow(id);
        shoppingList.getMenuIds().remove(menuId);
        return saveShoppingList(shoppingList);
    }

    public SavedShoppingListView addDishToShoppingList(String id, String dishId) {
        ShoppingListEntity shoppingList = findShoppingListOrThrow(id);
        validateDishExists(dishId);

        if (!shoppingList.getDishIds().contains(dishId)) {
            shoppingList.getDishIds().add(dishId);
        }
        return saveShoppingList(shoppingList);
    }

    public SavedShoppingListView removeDishFromShoppingList(String id, String dishId) {
        ShoppingListEntity shoppingList = findShoppingListOrThrow(id);
        shoppingList.getDishIds().remove(dishId);
        return saveShoppingList(shoppingList);
    }

    public SavedShoppingListView addProductToShoppingList(String id, AdditionalProduct product) {
        ShoppingListEntity shoppingList = findShoppingListOrThrow(id);
        Unit unit = validateManualProduct(product);
        ManualProductPurchase purchase = new ManualProductPurchase(
                idGenerator.generate(),
                product.productId(),
                product.amount(),
                unit
        );
        shoppingList.getManualProducts().add(purchase);
        return saveShoppingList(shoppingList);
    }

    public SavedShoppingListView updateShoppingListProduct(String id, String itemId, AdditionalProduct product) {
        ShoppingListEntity shoppingList = findShoppingListOrThrow(id);
        ManualProductPurchase purchase = findManualPurchaseOrThrow(shoppingList, itemId);
        Unit unit = validateManualProduct(product);
        ManualProductPurchase updatedPurchase = new ManualProductPurchase(
                purchase.id(),
                product.productId(),
                product.amount(),
                unit
        );
        int purchaseIndex = shoppingList.getManualProducts().indexOf(purchase);
        shoppingList.getManualProducts().set(purchaseIndex, updatedPurchase);
        return saveShoppingList(shoppingList);
    }

    public SavedShoppingListView removeProductFromShoppingList(String id, String itemId) {
        ShoppingListEntity shoppingList = findShoppingListOrThrow(id);
        ManualProductPurchase purchase = findManualPurchaseOrThrow(shoppingList, itemId);
        shoppingList.getManualProducts().remove(purchase);
        return saveShoppingList(shoppingList);
    }

    private SavedShoppingListView saveShoppingList(ShoppingListEntity shoppingList) {
        ShoppingListEntity savedShoppingList = shoppingListRepository.save(shoppingList);
        return toShoppingListView(savedShoppingList);
    }

    private SavedShoppingListView toShoppingListView(ShoppingListEntity shoppingList) {
        List<AdditionalProduct> manualProducts = shoppingList.getManualProducts().stream()
                .map(this::toAdditionalProduct)
                .toList();
        List<ShoppingListItem> shoppingItems = shoppingListService.calculateShoppingListWithMenus(
                shoppingList.getMenuIds(),
                shoppingList.getDishIds(),
                manualProducts
        );
        List<ManualPurchaseView> manualPurchases = shoppingList.getManualProducts().stream()
                .map(this::toManualPurchaseView)
                .toList();

        return new SavedShoppingListView(
                shoppingList.getId(),
                shoppingList.getName(),
                shoppingList.isInbox(),
                List.copyOf(shoppingList.getMenuIds()),
                List.copyOf(shoppingList.getDishIds()),
                manualPurchases,
                shoppingItems
        );
    }

    private AdditionalProduct toAdditionalProduct(ManualProductPurchase purchase) {
        return new AdditionalProduct(
                purchase.productId(),
                purchase.amount(),
                purchase.unit().name()
        );
    }

    private ManualPurchaseView toManualPurchaseView(ManualProductPurchase purchase) {
        return new ManualPurchaseView(
                purchase.id(),
                purchase.productId(),
                purchase.amount(),
                purchase.unit().name()
        );
    }

    private void validateDishExists(String dishId) {
        if (dishApi.getByIds(List.of(dishId)).isEmpty()) {
            throw new ShoppingDishNotFoundException("Dish not found: " + dishId);
        }
    }

    private Unit validateManualProduct(AdditionalProduct product) {
        if (product.amount() == null || product.amount().signum() <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (productApi.findById(product.productId()).isEmpty()) {
            throw new ShoppingProductNotFoundException(product.productId());
        }
        return Unit.valueOf(product.unit());
    }

    private ShoppingListEntity findShoppingListOrThrow(String id) {
        return shoppingListRepository.findById(id)
                .orElseThrow(() -> new ShoppingListNotFoundException(id));
    }

    private MenuView findMenuOrThrow(String menuId) {
        return menuApi.findById(menuId)
                .orElseThrow(() -> new ShoppingMenuNotFoundException(menuId + " not found"));
    }

    private ManualProductPurchase findManualPurchaseOrThrow(ShoppingListEntity shoppingList, String itemId) {
        return shoppingList.getManualProducts().stream()
                .filter(purchase -> purchase.id().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ShoppingListNotFoundException(itemId));
    }

    private void validateCustomList(ShoppingListEntity shoppingList) {
        if (shoppingList.isInbox()) {
            throw new InboxModificationException();
        }
    }

    private String normalizeListName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Shopping list name is required");
        }
        String normalizedName = name.strip();
        if (normalizedName.length() > MAX_LIST_NAME_LENGTH) {
            throw new IllegalArgumentException("Shopping list name is too long");
        }
        return normalizedName;
    }
}
