package MidTermProject.model.Accounts;

import MidTermProject.model.Users.AccountHolder;
import MidTermProject.model.Money;
import MidTermProject.model.Users.Status;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

@Entity
@PrimaryKeyJoinColumn(name="account_id")
public class Checking extends BasicAccount {

    @NotNull
    private String secretKey;
    @AttributeOverrides({
            @AttributeOverride(name="currency",column=@Column(name="minimum_balance_currency")),
            @AttributeOverride(name="amount",column=@Column(name="minimum_balance_amount"))
    })
    @Embedded
    private Money minimumBalance;
    @AttributeOverrides({
            @AttributeOverride(name="currency",column=@Column(name="monthly_fee_currency")),
            @AttributeOverride(name="amount",column=@Column(name="monthly_fee_amount"))
    })
    @Embedded
    private Money monthlyMaintenanceFee;
    @Enumerated(EnumType.STRING)
    @Column(name="status", columnDefinition="ENUM('FROZEN', 'ACTIVE')",nullable = false)
    private Status status;

    public Checking() {
    }

    public Checking(Money balance, AccountHolder primaryOwner, Optional<AccountHolder> secondaryOwner, String secretKey,
                    Money minimumBalance, Date creationDate, Status status) {

        super(balance, primaryOwner, secondaryOwner, creationDate);
        setBalance(balance);
        this.secretKey = secretKey;
        setMinimumBalance(minimumBalance);
        this.monthlyMaintenanceFee= new Money(BigDecimal.valueOf(12));
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
        if (minimumBalance.getAmount().compareTo(BigDecimal.valueOf(250)) == 1) throw new IllegalArgumentException("Minimum balance must be above 250");
        this.minimumBalance = minimumBalance;
    }

    public Money getMonthlyMaintenanceFee() {
        return monthlyMaintenanceFee;
    }

    public void setMonthlyMaintenanceFee(Money monthlyMaintenanceFee) {
        this.monthlyMaintenanceFee = monthlyMaintenanceFee;
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
            System.out.println("Penalty charged in account: "+super.getPenaltyFee()+"€");
        }else {
            super.setBalance(balance);
        }
    }
}
