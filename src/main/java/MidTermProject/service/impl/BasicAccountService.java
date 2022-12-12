package MidTermProject.service.impl;

import MidTermProject.controller.dto.AccountBalanceDTO;
import MidTermProject.model.Accounts.BasicAccount;
import MidTermProject.model.Accounts.Transactions;
import MidTermProject.model.Money;
import MidTermProject.model.Users.Status;
import MidTermProject.repository.BasicAccountRepository;
import MidTermProject.repository.CheckingRepository;
import MidTermProject.repository.TransactionsRepository;
import MidTermProject.service.interfaces.IBasicAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class BasicAccountService implements IBasicAccountService {

    @Autowired
    BasicAccountRepository basicAccountRepository;
    @Autowired
    CheckingRepository checkingRepository;
    @Autowired
    TransactionsRepository transactionsRepository;

    public void updateBalance(Money balance, Integer id) {
        Optional<BasicAccount> accountOptional = basicAccountRepository.findById(id);
        if (accountOptional.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        BasicAccount account = accountOptional.get();
        account.setBalance(balance);
        basicAccountRepository.save(account);
    }

    public void postTransferTime(Integer accountId, Money transferAmount) {
        Transactions t = new Transactions(basicAccountRepository.findById(accountId).get(), LocalDateTime.now(), transferAmount);
        transactionsRepository.save(t);
    }

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

    public void transferBalance(AccountBalanceDTO accountBalanceDTO,
                                Integer senderAccountId,
                                Integer receiverAccountId,
                                String receiverName) {
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
        if (  !(receiverName.equals(receiverAccount.getPrimaryOwner().getName()))  ){
            throw new
                    ResponseStatusException(HttpStatus.BAD_REQUEST,"The owner of the destination account " +
                    "does not match the name provided, the transfer won't take place");
        }

        //Comprobamos si hay fondos suficientes
        if (senderAccount.getBalance().getAmount().compareTo(accountBalanceDTO.getBalance().getAmount()) == 1){
            //Restamos la cantidad al balance de la cuenta que envía los fondos
            updateBalance(
                    new Money (senderAccount.getBalance().getAmount().subtract(accountBalanceDTO.getBalance().getAmount())),
                    senderAccountId);



            //Sumamos el balance a la cuenta que recibe los fondos
            updateBalance(
                    new Money (receiverAccount.getBalance().getAmount().add(accountBalanceDTO.getBalance().getAmount())),
                    receiverAccountId);

            //----------------------------------DETECCIÓN_FRAUDE----------------------------------------------
            //registramos la transacción con su fecha y hora
            postTransferTime(senderAccountId,accountBalanceDTO.getBalance());
            LocalDateTime t = LocalDateTime.now().minusHours(24);
            BigDecimal maxLast24hours = transactionsRepository.findMaxTransctLast24Hours(t, LocalDateTime.now()).get();

            //Cantidad 150% superior a la transacción más alta de las últimas 24 horas = 100%+150%=250% => cantidad transferencia*2.5
            BigDecimal criticalAmount = maxLast24hours.multiply(BigDecimal.valueOf(2.5));

            //si la cantidad de la transferencia es mayor a la crítica, se bloquea la cuenta
            if(accountBalanceDTO.getBalance().getAmount().compareTo(criticalAmount)==1){
                checkingRepository.findById(senderAccountId).get().setStatus(Status.FROZEN);
            }

            //borramos las transferencias anteriores a hace 24 horas la cuenta que envia dinero, ya que solo interesan
            // las transacciones de la cuenta de las últimas 24 horas
            transactionsRepository.deleteByTransferDateTimeLessThan(t, senderAccountId);


        }else{
            throw new
                    ResponseStatusException(HttpStatus.BAD_REQUEST,"Not enough funds in sender account");
        }

    }
}
