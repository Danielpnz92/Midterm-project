package MidTermProject.controller.dto;

import MidTermProject.model.Money;

import javax.validation.constraints.NotEmpty;

public class AccountBalanceDTO {

    private Money balance;

    public AccountBalanceDTO() {
    }

    public AccountBalanceDTO(Money balance) {
        this.balance = balance;
    }

    public Money getBalance() {
        return balance;
    }
}
