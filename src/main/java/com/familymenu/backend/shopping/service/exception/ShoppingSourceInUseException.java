package com.familymenu.backend.shopping.service.exception;

public class ShoppingSourceInUseException extends RuntimeException {

    public ShoppingSourceInUseException(String sourceId) {
        super("Remove this source from shopping lists before deleting it: " + sourceId);
    }
}
