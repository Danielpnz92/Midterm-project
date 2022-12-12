package MidTermProject.controller.impl;

import MidTermProject.controller.dto.AccountBalanceDTO;
import MidTermProject.controller.interfaces.IBasicAccountController;
import MidTermProject.model.Accounts.*;
import MidTermProject.model.Address;
import MidTermProject.model.Money;
import MidTermProject.model.Users.AccountHolder;
import MidTermProject.model.Users.Roles;
import MidTermProject.model.Users.Status;
import MidTermProject.repository.*;
import MidTermProject.service.impl.BasicAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class BasicAccountController implements IBasicAccountController {

    @Autowired
    BasicAccountRepository basicAccountRepository;
    @Autowired
    CheckingRepository checkingRepository;
    @Autowired
    SavingsRepository savingsRepository;
    @Autowired
    CreditCardRepository creditCardRepository;
    @Autowired
    StudentAccountRepository studentAccountRepository;
    @Autowired
    AccountHolderRepository accountHolderRepository;
    @Autowired
    BasicAccountService basicAccountService;


    @GetMapping("/accounts/all")
    @ResponseStatus(HttpStatus.OK)
    public List<BasicAccount> getAll(){

//        Optional<Address> address=Optional.empty();
//        Address ad = new Address("fdsf",23,"rdasfsa",8983);
//        AccountHolder ah = new AccountHolder(2, "test_user","pass", String.valueOf(Roles.ACCOUNT_HOLDER),
//                new Date(1992,8,13),ad, address);
//        Checking ch = new Checking(new Money(BigDecimal.valueOf(100)),ah,Optional.empty(),"secretkey",
//                new Money(BigDecimal.valueOf(50)),new Date(), Status.ACTIVE);
//        accountHolderRepository.save(ah);
//        checkingRepository.save(ch);
        return basicAccountRepository.findAll();
    }

    @GetMapping("/accounts/balance/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Money getAccountBalanceById(@PathVariable(name = "id") Integer accountId) {
        SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<BasicAccount> basicAccountOptional = basicAccountRepository.findById(accountId);
        if (basicAccountOptional.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Account not found");
        return basicAccountOptional.get().getBalance();
    }

    @GetMapping("/own_accounts/balance/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Money getOwnAccountBalance(@PathVariable(name = "id") Integer accountId) {
        return basicAccountService.getOwnAccountBalance(accountId);
    }

    @PatchMapping("/accounts/balance_modify/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBalance(@RequestBody @Valid AccountBalanceDTO accountBalanceDTO, @PathVariable Integer id) {
        basicAccountService.updateBalance(accountBalanceDTO.getBalance(),id);
    }

    @PatchMapping("/accounts/transfer/{senderAccountId}/{receiverAccountId}/{receiverName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void transferBalance(@RequestBody @Valid AccountBalanceDTO accountBalanceDTO,
                                @PathVariable(name = "senderAccountId") Integer senderAccountId,
                                @PathVariable(name = "receiverAccountId") Integer receiverAccountId,
                                @PathVariable(name = "receiverName") String receiverName) {
        basicAccountService.transferBalance(accountBalanceDTO,senderAccountId,receiverAccountId,receiverName);
    }

    @PatchMapping("/accounts/third_party/{amount}/{accountId}/{secretKey}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void thirdPartySendReceive(@PathVariable(name = "amount") Double amount,
                                      @PathVariable(name = "accountId") Integer accountId,
                                      @PathVariable(name = "secretKey") String secretKey) {
        Optional<BasicAccount> basicAccountOptional = basicAccountRepository.findById(accountId);
        if (basicAccountOptional.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Account not found");

        //Secret key pueden tener las cuentas de checking, student account y savings
        Optional<Checking> checkingOptional = checkingRepository.findById(accountId);
        Optional<StudentAccount> studentAccountOptional = studentAccountRepository.findById(accountId);
        Optional<Savings> savingsOptional = savingsRepository.findById(accountId);
        String secretKeyFromAccount;
        //se crea el objeto DTO para poder actualizar el balance de la cuenta mediante el servicio de BasicAccount
        AccountBalanceDTO accountBalanceDTO = new AccountBalanceDTO(new Money(BigDecimal.valueOf(amount)));

        //Se utiliza el increaseAmount() de Money dando por hecho que si se quiere sacar dinero de la cuenta, la
        //variable amount será negativa, y por tanto con es método restaría (suma de negativo)

        //se comprueba para cada optional de tipo de cuenta si existe (solo habrá uno de los 3 por ser imposible
        //compartir id de cuenta en cuentas diferentes), y se saca el valor de secretKey para validar que sea igual
        //al proporcionado en el método. Si es igual se actualiza la cuenta, sino da error
        if (!checkingOptional.isEmpty()) {
            System.out.println("checking");
            secretKeyFromAccount = checkingOptional.get().getSecretKey();
            if (!secretKeyFromAccount.equals(secretKey))
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Secret key mismatched with that account id");
            else basicAccountService.updateBalance(
                    new Money (checkingOptional.get().getBalance().getAmount().add(accountBalanceDTO.getBalance().getAmount())),
                    accountId);
        }
        if (!savingsOptional.isEmpty()) {
            System.out.println("savings");
            secretKeyFromAccount = savingsOptional.get().getSecretKey();
            if (!secretKeyFromAccount.equals(secretKey))
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Secret key mismatched with that account id");
            else basicAccountService.updateBalance(
                    new Money (savingsOptional.get().getBalance().getAmount().add(accountBalanceDTO.getBalance().getAmount())),
                    accountId);
        }
        if (!studentAccountOptional.isEmpty()) {
            System.out.println("student");
            secretKeyFromAccount = studentAccountOptional.get().getSecretKey();
            if (!secretKeyFromAccount.equals(secretKey))
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Secret key mismatched with that account id");
            else basicAccountService.updateBalance(
                    new Money (studentAccountOptional.get().getBalance().getAmount().add(accountBalanceDTO.getBalance().getAmount())),
                    accountId);
        }

    }

    @DeleteMapping("/accounts/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@PathVariable Integer id) {
        Optional<BasicAccount> basicAccountOptional = basicAccountRepository.findById(id);
        Optional<Savings> savingsOptional = savingsRepository.findById(id);
        Optional<Checking> checkingOptional = checkingRepository.findById(id);
        Optional<CreditCard> creditCardOptional = creditCardRepository.findById(id);
        Optional<StudentAccount> studentAccountOptional = studentAccountRepository.findById(id);

        if (!savingsOptional.isEmpty()) savingsRepository.deleteById(id);
        else if (!checkingOptional.isEmpty()) checkingRepository.deleteById(id);
        else if (!creditCardOptional.isEmpty()) creditCardRepository.deleteById(id);
        else if (!studentAccountOptional.isEmpty()) studentAccountRepository.deleteById(id);
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");

    }


}
