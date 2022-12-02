package MidTermProject.model.Users;

import javax.persistence.*;

@Entity
@PrimaryKeyJoinColumn(name="user_id")
public class Admin extends User {

    public Admin() {
    }

    public Admin(String name) {
        super(name);
    }
}
