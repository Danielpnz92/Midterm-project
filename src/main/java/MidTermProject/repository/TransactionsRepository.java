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

    @Query("SELECT t FROM Transactions t WHERE t.transferDateTime=?1 AND t.transferDateTime=?2")
    List<Transactions> findTransactToDelete(Integer id);

    @Modifying
    @Query("DELETE from Transactions t where t.transferDateTime BETWEEN {ts ?1} AND {ts ?2} AND t.id=?3")
    void deleteByTransferDateTimeLessThan(LocalDateTime dateTime1, LocalDateTime dateTime2, Integer id);

    @Query("SELECT MAX(t.transferAmount) FROM Transactions t WHERE t.transferDateTime BETWEEN {ts ?1} AND {ts ?2}")
    Optional<BigDecimal> findMaxTransctLast24Hours(LocalDateTime dateTime1, LocalDateTime dateTime2);
//    DateTime format in sql: {ts '2008-12-20 00:00:00'}
}

