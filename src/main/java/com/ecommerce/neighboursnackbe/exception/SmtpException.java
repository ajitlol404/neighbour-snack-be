package com.ecommerce.neighboursnackbe.exception;

public class SmtpException extends RuntimeException {

    public SmtpException() {
        super("SMTP error occurred");
    }

    public SmtpException(String message) {
        super(message);
    }

    public SmtpException(String message, Throwable cause) {
        super(message, cause);
    }

}