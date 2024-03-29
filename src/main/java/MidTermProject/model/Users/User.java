package MidTermProject.model.Users;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer userId;

    @NotNull
    private String name;
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(name="role", columnDefinition="ENUM('ADMIN', 'ACCOUNT_HOLDER', 'THIRD_PARTY')",nullable = false)
    private Roles role;

    public User(Integer id, String name, String password, Roles role) {
        this.userId=id;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Roles getRole() {
        return role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
