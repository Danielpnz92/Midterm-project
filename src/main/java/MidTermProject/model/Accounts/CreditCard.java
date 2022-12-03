package MidTermProject.model.Accounts;
import MidTermProject.model.Money;
import MidTermProject.model.Users.AccountHolder;
import com.sun.istack.NotNull;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

@Entity
@PrimaryKeyJoinColumn(name="user_id")
public class CreditCard extends BasicAccount{

    @NotNull
    private Money creditLimit;
    @NotNull
    private BigDecimal interestRate;

    public CreditCard() {
    }

    public CreditCard(Money balance, AccountHolder primaryOwner, Optional<AccountHolder> secondaryOwner, Date creationDate, Money creditLimit, BigDecimal interestRate) {
        super(balance, primaryOwner, secondaryOwner, creationDate);
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }

    public Money getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Money creditLimit) {
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
        if (interestRate.compareTo(BigDecimal.valueOf(0.1))==1 && interestRate.compareTo(BigDecimal.valueOf(0.2))==-1){
            this.interestRate = interestRate;
        }else{
            this.interestRate = BigDecimal.valueOf(0.2);
        }


    }
}
