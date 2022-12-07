package MidTermProject.controller.impl;

import MidTermProject.controller.interfaces.ICheckingController;
import MidTermProject.model.Accounts.Checking;
import MidTermProject.model.Accounts.StudentAccount;
import MidTermProject.repository.CheckingRepository;
import MidTermProject.repository.StudentAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CheckingController implements ICheckingController {

    @Autowired
    CheckingRepository checkingRepository;
    @Autowired
    StudentAccountRepository studentAccountRepository;

    @PostMapping("/accounts/checking")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveChecking(@RequestBody @Valid Checking account) {
        //calcular edad del propietario principal
        LocalDate dateOfBirth = LocalDate.of(account.getPrimaryOwner().getDateOfBirth().getYear(),
                account.getPrimaryOwner().getDateOfBirth().getMonth(),
                account.getPrimaryOwner().getDateOfBirth().getDay());
        LocalDate now = LocalDate.now();
        int age = Period.between(dateOfBirth, now).getYears();

        if (age>24){
            checkingRepository.save(account);
        }else{
            StudentAccount studentAccount = new StudentAccount(
                    account.getBalance(),account.getPrimaryOwner(), Optional.ofNullable(account.getSecondaryOwner()),
                    account.getSecretKey(),account.getCreationDate(), account.getStatus()
            );
            studentAccountRepository.save(studentAccount);
        }

    }
}
