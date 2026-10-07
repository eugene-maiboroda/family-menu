package com.familymenu.backend.shopping.service.exception;

public class InboxModificationException extends RuntimeException {
    public InboxModificationException() {
        super("Inbox cannot be renamed or deleted; clear its contents instead");
    }
}
