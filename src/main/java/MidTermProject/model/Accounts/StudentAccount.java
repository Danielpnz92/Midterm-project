package MidTermProject.model.Accounts;

import MidTermProject.model.Accounts.BasicAccount;
import MidTermProject.model.Money;
import MidTermProject.model.Users.AccountHolder;
import MidTermProject.model.Users.Status;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Date;
import java.util.Optional;

@Entity
@PrimaryKeyJoinColumn(name="account_id")
public class StudentAccount extends BasicAccount {

    @NotNull
    private String secretKey;
    @Enumerated(EnumType.STRING)
    @Column(name="status", columnDefinition="ENUM('FROZEN', 'ACTIVE')",nullable = false)
    private Status status;

    public StudentAccount() {
    }

    public StudentAccount(Money balance, AccountHolder primaryOwner, Optional<AccountHolder> secondaryOwner, String secretKey, Date creationDate, Status status) {
        super(balance, primaryOwner, secondaryOwner, creationDate);
        this.secretKey = secretKey;
        this.status = status;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
