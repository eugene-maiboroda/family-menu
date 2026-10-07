package com.familymenu.backend.shopping.service;

import com.familymenu.backend.dish.api.DishApi;
import com.familymenu.backend.menu.api.MenuApi;
import com.familymenu.backend.product.api.ProductApi;
import com.familymenu.backend.product.api.dto.ProductView;
import com.familymenu.backend.shared.IdGenerator;
import com.familymenu.backend.shopping.domain.ShoppingListItem;
import com.familymenu.backend.shopping.domain.Unit;
import com.familymenu.backend.shopping.persistence.ShoppingListEntity;
import com.familymenu.backend.shopping.persistence.ShoppingListRepository;
import com.familymenu.backend.shopping.service.dto.AdditionalProduct;
import com.familymenu.backend.shopping.service.exception.ShoppingListNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SavedShoppingListServiceTest {

    private ShoppingListEntity shoppingList;
    private ShoppingListService calculator;
    private SavedShoppingListService service;

    @BeforeEach
    void setUp() {
        shoppingList = new ShoppingListEntity();
        shoppingList.setId("weekly");
        shoppingList.setName("Weekly");
        shoppingList.getMenuIds().add("menu-1");

        ShoppingListRepository repository = mock(ShoppingListRepository.class);
        when(repository.findById("weekly")).thenReturn(Optional.of(shoppingList));
        when(repository.save(any(ShoppingListEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        calculator = mock(ShoppingListService.class);
        when(calculator.calculateShoppingListWithMenus(anyList(), anyList(), anyList()))
                .thenReturn(List.of(item("tomato", "1", Unit.KG), item("tomato", "2", Unit.PCS)));

        ProductApi productApi = mock(ProductApi.class);
        when(productApi.findById("tomato")).thenReturn(Optional.of(new ProductView("tomato", "Tomato")));
        service = new SavedShoppingListService(
                repository, calculator, mock(MenuApi.class), mock(DishApi.class), productApi, new IdGenerator()
        );
    }

    @Test
    void marksOnlyTheRequestedMeasurementAndSupportsUnchecking() {
        service.updateShoppingListItemPurchaseStatus("weekly", "tomato", Unit.Dimension.MASS, true);
        service.updateShoppingListItemPurchaseStatus("weekly", "tomato", Unit.Dimension.MASS, true);

        assertThat(service.getShoppingList("weekly").items())
                .extracting(ShoppingListItem::purchased)
                .containsExactly(true, false);
        assertThat(shoppingList.getPurchasedItems()).hasSize(1);

        service.updateShoppingListItemPurchaseStatus("weekly", "tomato", Unit.Dimension.MASS, false);
        assertThat(shoppingList.getPurchasedItems()).isEmpty();
    }

    @Test
    void resetPreservesSourcesAndQuantitiesForNextWeek() {
        service.updateShoppingListItemPurchaseStatus("weekly", "tomato", Unit.Dimension.MASS, true);

        assertThat(service.resetShoppingListPurchases("weekly").items())
                .extracting(ShoppingListItem::purchased)
                .containsExactly(false, false);
        assertThat(shoppingList.getMenuIds()).containsExactly("menu-1");
        assertThat(service.getShoppingList("weekly").items().getFirst().amount())
                .isEqualByComparingTo("1");
    }

    @Test
    void contentChangeResetsChangedAmountsButPreservesUnchangedItems() {
        service.updateShoppingListItemPurchaseStatus("weekly", "tomato", Unit.Dimension.MASS, true);
        service.updateShoppingListItemPurchaseStatus("weekly", "tomato", Unit.Dimension.COUNT, true);
        when(calculator.calculateShoppingListWithMenus(anyList(), anyList(), anyList()))
                .thenAnswer(invocation -> shoppingList.getManualProducts().isEmpty()
                        ? List.of(item("tomato", "1", Unit.KG), item("tomato", "2", Unit.PCS))
                        : List.of(item("tomato", "1.5", Unit.KG), item("tomato", "2", Unit.PCS)));

        service.addProductToShoppingList("weekly", new AdditionalProduct("tomato", new BigDecimal("500"), "G"));

        assertThat(service.getShoppingList("weekly").items())
                .extracting(ShoppingListItem::purchased)
                .containsExactly(false, true);
    }

    @Test
    void keepsPurchaseStatusWhenOnlyDisplayUnitOrDecimalScaleChanges() {
        service.updateShoppingListItemPurchaseStatus("weekly", "tomato", Unit.Dimension.MASS, true);
        when(calculator.calculateShoppingListWithMenus(anyList(), anyList(), anyList()))
                .thenAnswer(invocation -> shoppingList.getManualProducts().isEmpty()
                        ? List.of(item("tomato", "1.00", Unit.KG))
                        : List.of(item("tomato", "1000", Unit.G)));

        service.addProductToShoppingList("weekly", new AdditionalProduct("tomato", BigDecimal.ONE, "G"));

        assertThat(service.getShoppingList("weekly").items().getFirst().purchased()).isTrue();
    }

    @Test
    void rejectsMissingItemsAndClearsPurchaseMarksWhenContentsAreCleared() {
        assertThatThrownBy(() -> service.updateShoppingListItemPurchaseStatus(
                "weekly", "unknown", Unit.Dimension.MASS, true
        )).isInstanceOf(ShoppingListNotFoundException.class);
        service.updateShoppingListItemPurchaseStatus("weekly", "tomato", Unit.Dimension.MASS, true);

        service.clearShoppingList("weekly");

        assertThat(shoppingList.getPurchasedItems()).isEmpty();
        assertThat(shoppingList.getMenuIds()).isEmpty();
    }

    private ShoppingListItem item(String productId, String amount, Unit unit) {
        return new ShoppingListItem(productId, productId, new BigDecimal(amount), unit);
    }
}
