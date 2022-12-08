package MidTermProject.controller.impl;

import MidTermProject.controller.interfaces.IAccountHolderController;
import MidTermProject.model.Users.AccountHolder;
import MidTermProject.repository.AccountHolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AccountHolderController implements IAccountHolderController {

    @Autowired
    AccountHolderRepository accountHolderRepository;

    @GetMapping("/account_holder/all")
    @ResponseStatus(HttpStatus.OK)
    public List<AccountHolder> getAll(){

        return accountHolderRepository.findAll();
    }

}
