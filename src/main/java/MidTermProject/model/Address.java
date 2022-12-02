package MidTermProject.model;

import javax.persistence.Embeddable;

@Embeddable
public class Address {
    private String street;
    private Integer houseNumber;
    private String telephone;
    private Integer zipCode;


    public Address() {
    }

    public Address(String street, Integer houseNumber, String telephone, Integer zipCode) {
        this.street = street;
        this.houseNumber = houseNumber;
        this.telephone = telephone;
        this.zipCode = zipCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(Integer houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }
}
