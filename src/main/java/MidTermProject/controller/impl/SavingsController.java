package MidTermProject.controller.impl;

import MidTermProject.controller.interfaces.ISavingsController;
import MidTermProject.model.Accounts.Savings;
import MidTermProject.repository.SavingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class SavingsController implements ISavingsController {

    @Autowired
    SavingsRepository savingsRepository;

    @PostMapping("/accounts/savings")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveSavings(@RequestBody @Valid Savings account) {
        savingsRepository.save(account);
    }
}
