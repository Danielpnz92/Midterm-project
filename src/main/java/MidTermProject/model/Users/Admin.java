package MidTermProject.model.Users;

import javax.persistence.*;

@Entity
@PrimaryKeyJoinColumn(name="user_id")
public class Admin extends User {

    public Admin() {
    }

    public Admin(Integer userId, String name, String password, String role) {
        super(userId, name, password, Roles.ADMIN);
    }
}
