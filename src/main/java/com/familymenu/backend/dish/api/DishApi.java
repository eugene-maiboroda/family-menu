package com.familymenu.backend.dish.api;

import com.familymenu.backend.dish.api.dto.DishView;

import java.util.Collection;
import java.util.List;

public interface DishApi {

    List<DishView> getByIds(Collection<String> ids);
}
