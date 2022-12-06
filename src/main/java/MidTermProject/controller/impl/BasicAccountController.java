package MidTermProject.controller.impl;

import MidTermProject.controller.dto.AccountBalanceDTO;
import MidTermProject.controller.interfaces.IBasicAccountController;
import MidTermProject.model.Accounts.BasicAccount;
import MidTermProject.model.Money;
import MidTermProject.repository.BasicAccountRepository;
import MidTermProject.service.impl.BasicAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class BasicAccountController implements IBasicAccountController {

    @Autowired
    BasicAccountRepository basicAccountRepository;

    @Autowired
    BasicAccountService basicAccountService;

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
    public Money getOwnAccountBalance(Integer accountId) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        //Primero se busca si la cuenta pertenece al usuario actual como propietario principal
        Optional<BasicAccount> basicAccountOptional = basicAccountRepository.findAccountsOfCurrentUser1(userName, accountId);
        //Si no aparece ninguna, se busca si la cuenta existe para el usuario actual como usuario secuendario
        if (basicAccountOptional.isEmpty()) {
            basicAccountOptional = basicAccountRepository.findAccountsOfCurrentUser2(userName, accountId);
            if (basicAccountOptional.isEmpty()) throw new
                    ResponseStatusException(HttpStatus.NOT_FOUND,"Account not owned by user");
        }
        return basicAccountOptional.get().getBalance();
    }

    @PatchMapping("/accounts/balance_modify/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBalance(@RequestBody @Valid AccountBalanceDTO accountBalanceDTO, @PathVariable Integer id) {
        basicAccountService.updateBalance(accountBalanceDTO.getBalance(),id);
    }

    @PatchMapping("/accounts/transfer/{senderAccountId}/{receiverAccountId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void transferBalance(@RequestBody @Valid AccountBalanceDTO accountBalanceDTO,
                                @PathVariable Integer senderAccountId,
                                @PathVariable Integer receiverAccountId,
                                @PathVariable String receiverName) {
        //Nombre del usuario logueado haciendo la petición
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        //Primero se busca si la cuenta pertenece al usuario actual como propietario principal
        Optional<BasicAccount> basicAccountOptional = basicAccountRepository.findAccountsOfCurrentUser1(userName, senderAccountId);
        //Si no aparece ninguna, se busca si la cuenta existe para el usuario actual como usuario secuendario
        if (basicAccountOptional.isEmpty()) {
            basicAccountOptional = basicAccountRepository.findAccountsOfCurrentUser2(userName, senderAccountId);
            if (basicAccountOptional.isEmpty()) throw new
                    ResponseStatusException(HttpStatus.NOT_FOUND,"Account not owned by user");
        }

        BasicAccount senderAccount = basicAccountOptional.get();
        BasicAccount receiverAccount = basicAccountRepository.findById(receiverAccountId).get();

        //Comprobamos que el nombre del destinatario coincide con el de la cuenta especificada para la transferencia
        if (  !(receiverName == receiverAccount.getPrimaryOwner().getName() &&
                receiverName == receiverAccount.getSecondaryOwner().getName())  ){
            throw new
                    ResponseStatusException(HttpStatus.BAD_REQUEST,"The owner of the destination account" +
                    "does not match the name provided, the transfer won't take place");
        }

        //Comprobamos si hay fondos suficientes
        if (senderAccount.getBalance().getAmount().compareTo(accountBalanceDTO.getBalance().getAmount()) == 1){
            //Restamos la cantidad al balance de la cuenta que envía los fondos
            basicAccountService.updateBalance(
                    new Money (senderAccount.getBalance().getAmount().subtract(accountBalanceDTO.getBalance().getAmount())),
                    senderAccountId);

            //Sumamos el balance a la cuenta que recibe los fondos
            basicAccountService.updateBalance(
                    new Money (receiverAccount.getBalance().getAmount().add(accountBalanceDTO.getBalance().getAmount())),
                    receiverAccountId);
        }else{
            throw new
                    ResponseStatusException(HttpStatus.BAD_REQUEST,"Not enough funds in sender account");
        }

    }


}
