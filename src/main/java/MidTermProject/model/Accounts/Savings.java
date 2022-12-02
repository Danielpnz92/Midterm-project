package MidTermProject.model.Accounts;

import MidTermProject.model.Money;
import MidTermProject.model.Users.AccountHolder;
import MidTermProject.model.Users.Status;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@PrimaryKeyJoinColumn(name="user_id")
public class Savings extends BasicAccount{

    @NotNull
    private String secretKey;

    private Money minimumBalance;
    @NotNull
    private BigDecimal interestRate;
    @Enumerated(EnumType.STRING)
    @Column(name="status", columnDefinition="ENUM('FROZEN', 'ACTIVE')",nullable = false)
    private Status status;

    public Savings() {
    }

    public Savings(Money balance, AccountHolder primaryOwner, Date creationDate, String secretKey, Money minimumBalance, BigDecimal interestRate, Status status) {
        super(balance, primaryOwner, creationDate);
        this.secretKey = secretKey;
        setMinimumBalance(minimumBalance);
        setInterestRate(interestRate);
        this.status = status;
    }

    public Savings(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, Date creationDate, String secretKey, Money minimumBalance, BigDecimal interestRate, Status status) {
        super(balance, primaryOwner, secondaryOwner, creationDate);
        this.secretKey = secretKey;
        setMinimumBalance(minimumBalance);
        setInterestRate(interestRate);
        this.status = status;
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
        this.minimumBalance = minimumBalance;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
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
}
