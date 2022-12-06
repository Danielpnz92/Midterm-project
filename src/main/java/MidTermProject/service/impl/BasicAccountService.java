package MidTermProject.service.impl;

import MidTermProject.model.Accounts.BasicAccount;
import MidTermProject.model.Money;
import MidTermProject.repository.BasicAccountRepository;
import MidTermProject.service.interfaces.IBasicAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class BasicAccountService implements IBasicAccountService {

    @Autowired
    BasicAccountRepository basicAccountRepository;

    public void updateBalance(Money balance, Integer id) {
        Optional<BasicAccount> accountOptional = basicAccountRepository.findById(id);
        if (accountOptional.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        BasicAccount account = accountOptional.get();
        account.setBalance(balance);
        basicAccountRepository.save(account);
    }
}
