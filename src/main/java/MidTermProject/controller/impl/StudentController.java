package MidTermProject.controller.impl;

import MidTermProject.controller.interfaces.IStudentAccountController;
import MidTermProject.model.Accounts.StudentAccount;
import MidTermProject.repository.StudentAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class StudentController implements IStudentAccountController {

    @Autowired
    StudentAccountRepository studentAccountRepository;

    @PostMapping("/accounts/student_account")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveStudentAccount(@RequestBody @Valid StudentAccount account) {
        studentAccountRepository.save(account);
    }
}

