package MidTermProject.model.Accounts;

import MidTermProject.model.Users.AccountHolder;
import MidTermProject.model.Money;
import com.sun.istack.NotNull;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class BasicAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @AttributeOverrides({
            @AttributeOverride(name="currency",column=@Column(name="balance_currency")),
            @AttributeOverride(name="amount",column=@Column(name="balance_amount"))
    })
    @Embedded
    private Money balance;
    @ManyToOne
    @JoinColumn(name = "account_holder_id")
    @NotNull
    private AccountHolder primaryOwner;
    @ManyToOne
    @JoinColumn(name = "secondary_owner_id")
    private AccountHolder secondaryOwner;
    @NotNull
    private BigDecimal penaltyFee;
    @NotNull
    private Date creationDate;

    public BasicAccount() {
    }

    public BasicAccount(Money balance, AccountHolder primaryOwner, Optional<AccountHolder> secondaryOwner, Date creationDate) {
        this.balance = balance;
        this.primaryOwner = primaryOwner;
        if(secondaryOwner.isPresent()) this.secondaryOwner = secondaryOwner.get();
        this.creationDate = creationDate;
        setPenaltyFee(BigDecimal.valueOf(40.0));
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Money getBalance() {
        return balance;
    }

    public void setBalance(Money balance) {
        this.balance = balance;
    }

    public AccountHolder getPrimaryOwner() {
        return primaryOwner;
    }

    public void setPrimaryOwner(AccountHolder primaryOwner) {
        this.primaryOwner = primaryOwner;
    }

    public AccountHolder getSecondaryOwner() {
        return secondaryOwner;
    }

    public void setSecondaryOwner(AccountHolder secondaryOwner) {
        this.secondaryOwner = secondaryOwner;
    }

    public BigDecimal getPenaltyFee() {
        return penaltyFee;
    }

    public void setPenaltyFee(BigDecimal penaltyFee) {
        this.penaltyFee = penaltyFee;
    }

    public Integer getId() {
        return id;
    }
}
