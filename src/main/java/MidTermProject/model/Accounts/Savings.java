package MidTermProject.model.Accounts;

import MidTermProject.model.Money;
import MidTermProject.model.Users.AccountHolder;
import MidTermProject.model.Users.Status;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.Optional;

@Entity
@PrimaryKeyJoinColumn(name="user_id")
public class Savings extends BasicAccount{

    @NotNull
    private String secretKey;
    @AttributeOverrides({
            @AttributeOverride(name="currency",column=@Column(name="minimum_balance_currency")),
            @AttributeOverride(name="amount",column=@Column(name="minimum_balance_amount"))
    })
    @Embedded
    private Money minimumBalance;
    @NotNull
    private BigDecimal interestRate;
    @Enumerated(EnumType.STRING)
    @Column(name="status", columnDefinition="ENUM('FROZEN', 'ACTIVE')",nullable = false)
    private Status status;

    private int lastYearChecked;


    public Savings() {
    }

    public Savings(Money balance, AccountHolder primaryOwner, Optional<AccountHolder> secondaryOwner, Date creationDate,
                   String secretKey, Money minimumBalance, BigDecimal interestRate, Status status) {
        super(balance, primaryOwner, secondaryOwner, creationDate);
        this.secretKey = secretKey;
        setMinimumBalance(minimumBalance);
        setInterestRate(interestRate);
        this.status = status;
        this.lastYearChecked=creationDate.getYear();
        setBalance(balance);
    }

    public int getLastYearChecked() {
        return lastYearChecked;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Money getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(Money minimumBalance) {
        if (minimumBalance.getAmount().compareTo(BigDecimal.valueOf(100))==1){
            this.minimumBalance = minimumBalance;
        }else{
            this.minimumBalance = new Money(BigDecimal.valueOf(1000));
        }

    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        //si el tipo de interés a aplicar está entre 0% y 50% se admite, sino se pone por defecto 0.25%
        if (minimumBalance.getAmount().compareTo(BigDecimal.valueOf(0.5))==-1 && minimumBalance.getAmount().compareTo(BigDecimal.valueOf(0))==1){
            this.interestRate = interestRate;
        }else{
            this.interestRate = BigDecimal.valueOf(0.0025);
        }
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public void setBalance(Money balance) {
        if (balance.getAmount().compareTo(getMinimumBalance().getAmount())==-1){
            balance.setAmount(balance.getAmount().subtract(getPenaltyFee()));
            super.setBalance(balance);
        }else {
            super.setBalance(balance);
        }
    }

    @Override
    public Money getBalance() {
        if(lastYearChecked<LocalDate.now().getYear()){
            //Calculamos los años que han pasado desde la última fecha de comprobación hasta hoy
            int anyos = LocalDate.now().getYear()-lastYearChecked;
            //Balance acumulado será igual al balance base, por el interés elevado al número de años transcurridos desde
            //la fecha de última comprobación hasta hoy, ya que los intereses generados generan a su vez intereses.
            // Ej.: 10000*(1+0.01)^(2022-2018)
            BigDecimal cumulatedInterest = (getInterestRate().add(BigDecimal.valueOf(1))).pow(anyos);
            Money cumulatedBalance= new Money(super.getBalance().getAmount().multiply(cumulatedInterest));
            setBalance(cumulatedBalance);
            //se reasigna el valor al último año comprobado después de actualizar el balance
            lastYearChecked=LocalDate.now().getYear();
            return super.getBalance();
        }else{
            return super.getBalance();
        }
    }
}
