package MidTermProject.Methods;

import MidTermProject.model.Accounts.Checking;
import MidTermProject.model.Accounts.CreditCard;
import MidTermProject.model.Accounts.Savings;
import MidTermProject.model.Accounts.StudentAccount;
import MidTermProject.model.Money;
import MidTermProject.model.Users.AccountHolder;
import MidTermProject.model.Users.Status;
import MidTermProject.repository.AccountHolderRepository;
import MidTermProject.repository.UserRepository;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;

import static MidTermProject.Methods.Colors.ANSI_CYAN;

public class Utils {

    UserRepository userRepository;
    AccountHolderRepository accountHolderRepository;

    public void logIn() {
        KeyboardInput k = new KeyboardInput();
        System.out.println(ANSI_CYAN + "Please log in the application:");
        System.out.print("Username: ");
        System.out.println();
        System.out.print("Password");
    }

    //Admin
    public void createAccount(){
        KeyboardInput k = new KeyboardInput();
        System.out.println();
        System.out.println("1. Savings");
        System.out.println("2. Checking");
        System.out.println("3. Credit card");
        int opt = k.readInteger("What type of account you want to create?",
                "You must enter an integer number",1,3);
        switch (opt){
            case 1://Money balance, AccountHolder primaryOwner, Optional<AccountHolder> secondaryOwner, Date creationDate,
                   //String secretKey, Money minimumBalance, BigDecimal interestRate, Status status

                double minimumBalanceDouble = 0;
                double interestRateDouble = 0;
                int secUserId = 0;
                int userId = k.readInteger("Enter the id of the owner",
                        "You must enter an id from a valid user",
                        1,userRepository.findMax().get().getUserId());
                boolean so = k.readBoolean("Is there a secondary owner for this account?",
                        "Please specify whether 'true' or 'false'");

                double balanceDouble = k.readDouble("Enter the starting balance","the value is wrong");

                String secretKey = k.readString("Enter a secret key", "Enter a valid input");

                if (k.readBoolean("Do you want to set a minimum Balance for the account?",
                        "Please specify whether 'true' or 'false'")){
                    minimumBalanceDouble = k.readDouble("Enter the starting balance",
                            "the value is wrong");
                }

                if (k.readBoolean("Do you want to set an interest rate?",
                        "Please specify whether 'true' or 'false'")){
                    interestRateDouble = k.readDouble("Enter the interest rate",
                            "the value is wrong");
                }

                if (k.readBoolean("Is there a secondary owner for this account?",
                        "Please specify whether 'true' or 'false'")){
                    //El id del otro propietario no puede ser igual que el del principal
                    secUserId=userId;
                    while (secUserId == userId){
                        secUserId= k.readInteger("Enter the id of the secondary owner",
                                "You must enter a valid id", 1,userRepository.findMax().get().getUserId());
                    }
                }

                Savings sa = new Savings(new Money(BigDecimal.valueOf(balanceDouble)),
                        accountHolderRepository.findById(userId).get(),
                        accountHolderRepository.findById(secUserId), Date.valueOf(LocalDate.now()),secretKey,
                        new Money(BigDecimal.valueOf(minimumBalanceDouble)), BigDecimal.valueOf(interestRateDouble),
                        Status.ACTIVE);


                break;
            case 2: //Money balance, AccountHolder primaryOwner, Optional<AccountHolder> secondaryOwner, String secretKey,
//                    Money minimumBalance, Date creationDate, Status status

                minimumBalanceDouble = 0;
                secUserId = 0;
                userId = k.readInteger("Enter the id of the owner",
                        "You must enter an id from a valid user",
                        1,userRepository.findMax().get().getUserId());
                boolean so = k.readBoolean("Is there a secondary owner for this account?",
                        "Please specify whether 'true' or 'false'");

                balanceDouble = k.readDouble("Enter the starting balance","the value is wrong");

                secretKey = k.readString("Enter a secret key", "Enter a valid input");

                if (k.readBoolean("Do you want to set a minimum Balance for the account?",
                        "Please specify whether 'true' or 'false'")){
                    minimumBalanceDouble = k.readDouble("Enter the starting balance",
                            "the value is wrong");
                    Money minimumBalance = new Money(BigDecimal.valueOf(minimumBalanceDouble));
                }

                if (k.readBoolean("Is there a secondary owner for this account?",
                        "Please specify whether 'true' or 'false'")){
                    //El id del otro propietario no puede ser igual que el del principal
                    secUserId=userId;
                    while (secUserId == userId){
                        secUserId= k.readInteger("Enter the id of the secondary owner",
                                "You must enter a valid id", 1,userRepository.findMax().get().getUserId());
                    }
                }

                //calcular edad del propietario principal
                Date fechaNac = (Date) accountHolderRepository.findById(userId).get().getDateOfBirth();
                int edad = Period.between(LocalDate.now(), LocalDate.of(fechaNac.getYear(), fechaNac.getMonth(), fechaNac.getDay());
                if (edad>24){
                    Checking ch = new Checking(new Money(BigDecimal.valueOf(balanceDouble)),
                            accountHolderRepository.findById(userId).get(), accountHolderRepository.findById(secUserId),
                            secretKey, new Money(BigDecimal.valueOf(minimumBalanceDouble)), Date.valueOf(LocalDate.now()),
                            Status.ACTIVE);
                }else{
                    StudentAccount stAc = new StudentAccount(new Money(BigDecimal.valueOf(balanceDouble)),
                            accountHolderRepository.findById(userId).get(), accountHolderRepository.findById(secUserId),
                            secretKey, Date.valueOf(LocalDate.now()), Status.ACTIVE);
                }

                break;
            case 3: //Money balance, AccountHolder primaryOwner, Optional<AccountHolder> secondaryOwner,
                    //Date creationDate, Money creditLimit, BigDecimal interestRate
                double creditLimit = 0;
                interestRateDouble = 0;
                secUserId = 0;
                userId = k.readInteger("Enter the id of the owner",
                        "You must enter an id from a valid user",
                        1,userRepository.findMax().get().getUserId());
                so = k.readBoolean("Is there a secondary owner for this account?",
                        "Please specify whether 'true' or 'false'");

                balanceDouble = k.readDouble("Enter the starting balance","the value is wrong");

                if (k.readBoolean("Do you want to set a credit limit for the account?",
                        "Please specify whether 'true' or 'false'")){
                    creditLimit = k.readDouble("Enter the starting balance",
                            "the value is wrong");
                }

                if (k.readBoolean("Do you want to set an interest rate?",
                        "Please specify whether 'true' or 'false'")){
                    interestRateDouble = k.readDouble("Enter the interest rate",
                            "the value is wrong");
                }

                if (k.readBoolean("Is there a secondary owner for this account?",
                        "Please specify whether 'true' or 'false'")){
                    //El id del otro propietario no puede ser igual que el del principal
                    secUserId=userId;
                    while (secUserId == userId){
                        secUserId= k.readInteger("Enter the id of the secondary owner",
                                "You must enter a valid id", 1,userRepository.findMax().get().getUserId());
                    }
                }

                CreditCard cc = new CreditCard(new Money(BigDecimal.valueOf(balanceDouble)),
                        accountHolderRepository.findById(userId).get(),
                        accountHolderRepository.findById(secUserId), Date.valueOf(LocalDate.now()),
                        new Money(BigDecimal.valueOf(creditLimit)), BigDecimal.valueOf(interestRateDouble));
                break;
        }


    }


}
