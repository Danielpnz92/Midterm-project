package MidTermProject.controller.impl;

import MidTermProject.controller.interfaces.ICheckingController;
import MidTermProject.model.Accounts.Checking;
import MidTermProject.repository.CheckingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class CheckingController implements ICheckingController {

    @Autowired
    CheckingRepository checkingRepository;

    @PostMapping("/accounts/checking")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveChecking(@RequestBody @Valid Checking account) {
        checkingRepository.save(account);
    }
}
