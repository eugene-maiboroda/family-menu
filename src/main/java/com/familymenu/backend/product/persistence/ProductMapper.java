package com.familymenu.backend.product.persistence;

import com.familymenu.backend.product.api.dto.ProductView;
import com.familymenu.backend.product.domain.Product;
import com.familymenu.backend.infrastructure.config.MapStructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface ProductMapper {

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ProductEntity toEntity(Product ingredient);

    Product toDomain(ProductEntity entity);

    ProductView toView(Product ingredient);

    default ProductView toView(ProductEntity entity) {
        return toView(toDomain(entity));
    }
}
