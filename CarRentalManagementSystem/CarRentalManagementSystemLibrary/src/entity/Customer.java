/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author khoojingzhi
 */
@Entity
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    private String passportNumber;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String password;
    
    @OneToMany(mappedBy = "customer")
    private List<CarRentalReservationRecord> carRentalReservationRecords;
    @OneToOne(mappedBy = "customer")
    @JoinColumn(nullable = false)
    private CreditCard creditCard;
    

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Customer() {
        this.carRentalReservationRecords = new ArrayList<CarRentalReservationRecord>();
    }

    public Customer(String email, String phoneNumber, String passportNumber, String name, String password, CreditCard creditCard) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.passportNumber = passportNumber;
        this.name = name;
        this.password = password;
        this.creditCard = creditCard;
        this.carRentalReservationRecords = new ArrayList<CarRentalReservationRecord>();
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customerId != null ? customerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the customerId fields are not set
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.customerId == null && other.customerId != null) || (this.customerId != null && !this.customerId.equals(other.customerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Customer[ id=" + customerId + " ]";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<CarRentalReservationRecord> getCarRentalReservationRecords() {
        return carRentalReservationRecords;
    }

    public void setCarRentalReservationRecords(List<CarRentalReservationRecord> carRentalReservationRecords) {
        this.carRentalReservationRecords = carRentalReservationRecords;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }
    
}
