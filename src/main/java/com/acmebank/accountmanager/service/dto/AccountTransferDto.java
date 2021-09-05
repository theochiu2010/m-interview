package com.acmebank.accountmanager.service.dto;

public class AccountTransferDto {
    private String message;

    public AccountTransferDto(String message) {
        this.message = message;
    }


    public String getMessage() {
        return message;
    }
}
