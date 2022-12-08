package MidTermProject.model.Users;

import javax.persistence.*;

@Entity
@PrimaryKeyJoinColumn(name="user_id")
public class ThirdParty extends User{

    public ThirdParty() {
    }

    public ThirdParty(Integer userId, String name, String password, String role) {
        super(name, password, Roles.THIRD_PARTY);
    }
}
