package MidTermProject.model.Accounts;

import MidTermProject.model.Money;
import MidTermProject.model.Users.AccountHolder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Entity
@NoArgsConstructor
public class Transactions{

    //detección de fraude: congelar la cuenta si se hace una transacción de más del 150% de la transacción más alta en
    //en un período de 24 horas

    //ocurren más de 2 transacciones en un período de 1 segundo en la misma cuenta

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transactionId;
    @ManyToOne
    @JoinColumn(name = "id")
    private BasicAccount account;

    private LocalDateTime transferDateTime;
    @AttributeOverrides({
            @AttributeOverride(name="currency",column=@Column(name="transfer_currency")),
            @AttributeOverride(name="amount",column=@Column(name="transfer_amount"))
    })
    @Embedded
    private Money transferAmount;

    public Transactions(BasicAccount account, LocalDateTime transferDateTime, Money transferAmount) {
        this.account = account;
        this.transferDateTime = transferDateTime;
        this.transferAmount = transferAmount;
    }
}
