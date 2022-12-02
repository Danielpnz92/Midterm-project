package MidTermProject.model.Users;

import MidTermProject.model.Accounts.BasicAccount;
import MidTermProject.model.Address;
import MidTermProject.model.Users.User;
import com.sun.istack.NotNull;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@PrimaryKeyJoinColumn(name="user_id")
public class AccountHolder extends User {

    @NotNull
    private Date dateOfBirth;

    @Embedded
    @NotNull
    private Address primaryAddress;

//    private String street;
//    private Integer houseNumber;
//    private String telephone;
//    private Integer zipCode;

    @AttributeOverrides({
            @AttributeOverride(name="street",column=@Column(name="mailing_street")),
            @AttributeOverride(name="houseNumber",column=@Column(name="mailing_house_number")),
            @AttributeOverride(name="telephone",column=@Column(name="mailing_telephone")),
            @AttributeOverride(name="zipCode",column=@Column(name="mailing_zip_code"))
    })
    @Embedded
    private Address mailingAddress;

    @OneToMany(mappedBy = "primaryOwner")
    @ToString.Exclude
    private Set<BasicAccount> primaryAccounts;

    @OneToMany(mappedBy = "secondaryOwner")
    @ToString.Exclude
    private Set<BasicAccount> secondaryAccounts;

    public AccountHolder() {
    }

    public AccountHolder(String name, Date dateOfBirth, Address primaryAddress, Address mailingAddress) {
        super(name);
        this.dateOfBirth = dateOfBirth;
        this.primaryAddress = primaryAddress;
        this.mailingAddress = mailingAddress;
    }

    public AccountHolder(String name, Date dateOfBirth, Address primaryAddress) {
        super(name);
        this.dateOfBirth = dateOfBirth;
        this.primaryAddress = primaryAddress;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Address getPrimaryAddress() {
        return primaryAddress;
    }

    public void setPrimaryAddress(Address primaryAddress) {
        this.primaryAddress = primaryAddress;
    }

    public Address getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(Address mailingAddress) {
        this.mailingAddress = mailingAddress;
    }
}
