package MidTermProject.model.Accounts;
import MidTermProject.model.Money;
import MidTermProject.model.Users.AccountHolder;
import com.sun.istack.NotNull;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

@Entity
@PrimaryKeyJoinColumn(name="user_id")
public class CreditCard extends BasicAccount{

    @NotNull
    private Money creditLimit;
    @NotNull
    private BigDecimal interestRate;
    private int lastPeriodCheck;
    private int lastYearChecked;

    public CreditCard() {
    }

    public CreditCard(Money balance, AccountHolder primaryOwner, Optional<AccountHolder> secondaryOwner, Date creationDate, Money creditLimit, BigDecimal interestRate) {
        super(balance, primaryOwner, secondaryOwner, creationDate);
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
        this.lastPeriodCheck= creationDate.getMonth();
        this.lastYearChecked=creationDate.getYear();
    }

    public Money getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Money creditLimit) {
        //si el límite de crédito a aplicar está entre 100 y 100000 se admite, sino se pone por defecto 100
        if (creditLimit.getAmount().compareTo(BigDecimal.valueOf(100))==1 && creditLimit.getAmount().compareTo(BigDecimal.valueOf(100000))==-1){
            this.creditLimit = creditLimit;
        }else{
            this.creditLimit = new Money(BigDecimal.valueOf(100));
        }
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        //si el tipo de interés a aplicar está entre 0.1% y 0.2% se admite, sino se pone por defecto 0.2%
        if (interestRate.compareTo(BigDecimal.valueOf(0.001))==1 && interestRate.compareTo(BigDecimal.valueOf(0.002))==-1){
            this.interestRate = interestRate;
        }else{
            this.interestRate = BigDecimal.valueOf(0.002);
        }
    }

    @Override
    public Money getBalance() {
        if(lastPeriodCheck< LocalDate.now().getMonthValue()){
            //Calculamos los meses que han pasado desde la última fecha de comprobación hasta hoy.
            //Ej.: (2022-2022)*12+(12-8)= 4 meses / (2022-2020)*12+(12-6)=30
            int periods = (LocalDate.now().getYear()-lastYearChecked)*12+(LocalDate.now().getMonthValue()-lastPeriodCheck);
            //Balance acumulado será igual al balance base, por el interés elevado al número de meses transcurridos
            //desde la fecha de última comprobación hasta hoy, ya que los intereses generados generan a su
            //vez intereses.
            // Ej.: 10000*(1+0.002)^(4)
            BigDecimal cumulatedInterest = (getInterestRate().add(BigDecimal.valueOf(1))).pow(periods);
            Money cumulatedBalance= new Money(super.getBalance().getAmount().multiply(cumulatedInterest));
            setBalance(cumulatedBalance);
            //se reasigna el valor al último período comprobado después de actualizar el balance
            lastYearChecked=LocalDate.now().getYear();
            lastPeriodCheck=LocalDate.now().getMonthValue();
            return super.getBalance();
        }else {
            return super.getBalance();
        }
    }
}


