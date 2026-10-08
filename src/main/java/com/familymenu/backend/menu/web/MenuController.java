package com.familymenu.backend.menu.web;

import com.familymenu.backend.menu.api.dto.MenuView;
import com.familymenu.backend.menu.domain.Menu;
import com.familymenu.backend.menu.service.MenuService;
import com.familymenu.backend.menu.web.dto.CreateMenuRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;
    private final MenuWebMapper menuWebMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MenuView createMenu(@Valid @RequestBody CreateMenuRequest request) {
        Menu menu = menuService.createMenu(menuWebMapper.toDto(request));
        return toMenuView(menu);
    }

    @GetMapping
    public List<MenuView> listMenus(@RequestParam(required = false) String query) {
        return menuService.getMenus(query).stream()
                .map(this::toMenuView)
                .toList();
    }

    @GetMapping("/{id}")
    public MenuView getMenu(@PathVariable String id) {
        return toMenuView(menuService.getMenu(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMenu(@PathVariable String id) {
        menuService.getMenu(id);
        menuService.deleteMenu(id);
    }

    private MenuView toMenuView(Menu menu) {
        return new MenuView(menu.id(), menu.name(), menu.createdDate(), menu.dishIds());
    }
}
