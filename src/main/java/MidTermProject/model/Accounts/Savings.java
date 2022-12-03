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

    private Money minimumBalance;
    @NotNull
    private BigDecimal interestRate;
    @Enumerated(EnumType.STRING)
    @Column(name="status", columnDefinition="ENUM('FROZEN', 'ACTIVE')",nullable = false)
    private Status status;

    public Savings() {
    }

    public Savings(Money balance, AccountHolder primaryOwner, Optional<AccountHolder> secondaryOwner, Date creationDate, String secretKey, Money minimumBalance, BigDecimal interestRate, Status status) {
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
        //Calculamos los años que han pasado desde la fecha de creación hasta hoy
        LocalDate fechaCre = LocalDate.of(getCreationDate().getYear(), getCreationDate().getMonth(), getCreationDate().getDay());
        LocalDate hoy = LocalDate.now();
        Period anyos = Period.between(hoy, fechaCre);
        //Balance acumulado será igual balance base, por el interés elevado al número de años transcurridos desde
        //la creación de la cuenta, ya que los intereses generados generan a su vez intereses
        BigDecimal cumulatedInterest = getInterestRate().pow(anyos.getYears());
        Money cumulatedBalance= new Money(super.getBalance().getAmount().multiply(cumulatedInterest));
        //Comprobamos si el balance actual es igual al calculado, para no volver a añadir la cantidad correspondiente
        if(super.getBalance().getAmount().compareTo(cumulatedBalance.getAmount())!=0){
            setBalance(cumulatedBalance);
            return super.getBalance();
        }else{
            return super.getBalance();
        }



        //falta ver cómo hacer para que se aplique solo una vez cuando ya se ha consultado ese año, ahora mismo
        //no lo hace así
    }
}
