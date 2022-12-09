package MidTermProject.model.Accounts;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import java.time.LocalDateTime;

@Entity
@PrimaryKeyJoinColumn(name="account_id")
public class Transactions extends BasicAccount{
    private LocalDateTime lastTimeTransfer;

}
