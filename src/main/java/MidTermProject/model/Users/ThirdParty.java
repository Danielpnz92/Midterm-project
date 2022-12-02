package MidTermProject.model.Users;

import javax.persistence.*;

@Entity
@PrimaryKeyJoinColumn(name="user_id")
public class ThirdParty extends User{

    public ThirdParty() {
    }

    public ThirdParty(String name) {
        super(name);
    }
}
