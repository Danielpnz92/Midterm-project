package MidTermProject.repository;

import MidTermProject.model.Users.AccountHolder;
import MidTermProject.model.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE u.userId = (SELECT MAX(us.userId) FROM User us)")
    Optional<User> findMax();

    @Query("SELECT ah FROM AccountHolder ah WHERE ah.userId = ?1")
    Optional<AccountHolder> findMax(Integer id);

}
