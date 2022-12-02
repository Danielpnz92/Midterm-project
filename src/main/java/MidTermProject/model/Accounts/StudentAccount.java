package MidTermProject.model.Accounts;

import MidTermProject.model.Accounts.BasicAccount;
import MidTermProject.model.Money;
import MidTermProject.model.Users.AccountHolder;
import MidTermProject.model.Users.Status;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Date;

@Entity
@PrimaryKeyJoinColumn(name="user_id")
public class StudentAccount extends BasicAccount {

    @NotNull
    private String secretKey;
    @Enumerated(EnumType.STRING)
    @Column(name="status", columnDefinition="ENUM('FROZEN', 'ACTIVE')",nullable = false)
    private Status status;

    public StudentAccount() {
    }

    public StudentAccount(Money balance, AccountHolder primaryOwner, String secretKey, Date creationDate, Status status) {
        super(balance, primaryOwner, creationDate);
        this.secretKey = secretKey;
        this.status = status;
    }

    public StudentAccount(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey, Date creationDate, Status status) {
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
