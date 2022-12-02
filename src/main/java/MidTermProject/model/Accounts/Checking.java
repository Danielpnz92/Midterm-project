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

@Entity
@PrimaryKeyJoinColumn(name="user_id")
public class Checking extends BasicAccount {

    @NotNull
    private String secretKey;

    private Money minimumBalance;
    @NotNull
    private Money monthlyMaintenanceFee;
    @Enumerated(EnumType.STRING)
    @Column(name="status", columnDefinition="ENUM('FROZEN', 'ACTIVE')",nullable = false)
    private Status status;

    public Checking() {
    }

    public Checking(Money balance, AccountHolder primaryOwner, String secretKey, Money minimumBalance,
                    Date creationDate, Status status) {
        super(balance, primaryOwner, creationDate);
        this.secretKey = secretKey;
        this.minimumBalance.setAmount(minimumBalance.getAmount());
        this.monthlyMaintenanceFee.setAmount(BigDecimal.valueOf(12));
        this.status = status;
    }

    public Checking(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey,
                    Money minimumBalance, Date creationDate, Status status) {
        super(balance, primaryOwner, secondaryOwner, creationDate);
        this.secretKey = secretKey;
        this.minimumBalance.setAmount(minimumBalance.getAmount());
        this.monthlyMaintenanceFee.setAmount(BigDecimal.valueOf(12));
        this.status = status;
    }

    public Integer validateAge (AccountHolder owner){
        LocalDate fechaNac = LocalDate.of(owner.getDateOfBirth().getYear(), owner.getDateOfBirth().getMonth(), owner.getDateOfBirth().getDay());
        LocalDate hoy = LocalDate.now();
        Period anyos = Period.between(fechaNac, hoy);
        return anyos.getYears();
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
        }else {
            super.setBalance(balance);
        }
    }
}
