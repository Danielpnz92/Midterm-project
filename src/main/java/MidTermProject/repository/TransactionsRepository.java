package MidTermProject.repository;

import MidTermProject.model.Accounts.BasicAccount;
import MidTermProject.model.Accounts.Transactions;
import MidTermProject.model.Money;
import MidTermProject.model.Users.ThirdParty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionsRepository extends JpaRepository<Transactions, Integer> {

    Optional<BasicAccount> findByAccountId(Integer id);

    @Modifying
    @Query("DELETE from Transactions t where t.transferDateTime < ?1 AND t.id = ?2")
    void deleteByTransferDateTimeLessThan(LocalDateTime dateTime1, Integer id);

    @Query("SELECT MAX(t.transferAmount) FROM Transactions t WHERE t.transferDateTime BETWEEN ?1 AND ?2")
    Optional<BigDecimal> findMaxTransctLast24Hours(LocalDateTime dateTime1, LocalDateTime dateTime2);
//    DateTime format in sql: {ts '2008-12-20 00:00:00'}
}

