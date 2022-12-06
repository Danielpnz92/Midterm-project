package MidTermProject.repository;

import MidTermProject.model.Accounts.BasicAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Optional;

@Repository
public interface BasicAccountRepository extends JpaRepository<BasicAccount, Integer> {

    @Query("SELECT b FROM BasicAccount b JOIN User u ON b.primaryOwner=u.userId WHERE u.name=?1 AND b.id=?2")
    Optional<BasicAccount> findAccountsOfCurrentUser1(String name, Integer id);

    @Query("SELECT b FROM BasicAccount b JOIN User u ON b.secondaryOwner=u.userId WHERE u.name=?1 AND b.id=?2")
    Optional<BasicAccount> findAccountsOfCurrentUser2(String name, Integer id);




}
