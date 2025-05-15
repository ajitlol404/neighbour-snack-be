package com.ecommerce.neighboursnackbe.exception;

public class ProductException extends RuntimeException {

    public ProductException() {
        super("Product error occurred");
    }

    public ProductException(String message) {
        super(message);
    }

    public ProductException(String message, Throwable cause) {
        super(message, cause);
    }

}