package MidTermProject.controller.interfaces;

import MidTermProject.controller.dto.AccountBalanceDTO;
import MidTermProject.model.Accounts.BasicAccount;
import MidTermProject.model.Money;
import MidTermProject.service.impl.BasicAccountService;

import java.math.BigDecimal;
import java.util.Optional;

public interface IBasicAccountController {
    Money getAccountBalanceById(Integer accountId);
    Money getOwnAccountBalance(Integer accountId);
    void updateBalance(AccountBalanceDTO accountBalanceDTO, Integer id);

    void transferBalance(AccountBalanceDTO accountBalanceDTO, Integer senderAccountId,
                         Integer receiverAccountId, String receiverName);

    void thirdPartySendReceive(Double amount, Integer accountId, String secretKey);

    void deleteAccount(Integer id);
}
