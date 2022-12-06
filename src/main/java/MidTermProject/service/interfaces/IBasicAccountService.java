package MidTermProject.service.interfaces;

import MidTermProject.controller.dto.AccountBalanceDTO;
import MidTermProject.model.Accounts.BasicAccount;
import MidTermProject.model.Money;

import java.util.List;

public interface IBasicAccountService {
    void updateBalance(Money balance, Integer id);
}
