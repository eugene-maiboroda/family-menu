package com.familymenu.backend.menu.api;

import com.familymenu.backend.menu.api.dto.MenuView;

import java.util.Optional;

public interface MenuApi {

    Optional<MenuView> findById(String id);
}
