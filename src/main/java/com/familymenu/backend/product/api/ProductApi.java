package com.familymenu.backend.product.api;

import com.familymenu.backend.product.api.dto.ProductView;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProductApi {

    ProductView resolveByName(String name);

    Optional<ProductView> findById(String id);

    List<ProductView> getByIds(Collection<String> ids);
}
