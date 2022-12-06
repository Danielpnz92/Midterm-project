package MidTermProject.controller.dto;

import MidTermProject.model.Money;

import javax.validation.constraints.NotEmpty;

public class AccountBalanceDTO {

    @NotEmpty
    private Money balance;

    public Money getBalance() {
        return balance;
    }
}
