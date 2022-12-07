package MidTermProject.controller.impl;

import MidTermProject.controller.interfaces.ICreditCardController;
import MidTermProject.model.Accounts.Checking;
import MidTermProject.model.Accounts.CreditCard;
import MidTermProject.repository.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class CreditCardController implements ICreditCardController {

    @Autowired
    CreditCardRepository creditCardRepository;

    @PostMapping("/accounts/credit_card")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveCredit(@RequestBody @Valid CreditCard account) {
        creditCardRepository.save(account);
    }
}

