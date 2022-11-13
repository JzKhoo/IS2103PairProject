/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author khoojingzhi
 */
@Entity
public class CarRentalReservationRecord implements Serializable {

    // Attributes
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carRentalReservationRecordId;
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date pickupDate;
    @Column(nullable = false)
    private String pickupLocation;
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date returnDate;
    @Column(nullable = false)
    private String returnLocation;
    
    private String categoryChoice;
    private String makeChoice;
    private String modelChoice;
    
    @Column(nullable = false)
    private boolean isCancelled = false;
    @Column(nullable = false)
    private boolean isPaid;
    @Column(nullable = false)
    private String creditCardNumber;
    @Column(nullable = false)
    private String cvv;
    private int price;
    
    // Relationships
    @ManyToOne
    private Car car;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Customer customer;
    @ManyToMany(mappedBy = "carRentalReservationRecords")
    private List<RentalRate> rentalRates;

    public Long getCarRentalReservationRecordId() {
        return carRentalReservationRecordId;
    }

    public void setCarRentalReservationRecordId(Long carRentalReservationRecordId) {
        this.carRentalReservationRecordId = carRentalReservationRecordId;
    }

    
    // Constructor
    public CarRentalReservationRecord() {
        this.rentalRates = new ArrayList<>();
    }

    public CarRentalReservationRecord(Date pickupDate, String pickupLocation, Date returnDate, String returnLocation, boolean isPaid, String creditCardNumber, String cvv, Customer customer, List<RentalRate> rentalRates) {
        this();
        
        this.pickupDate = pickupDate;
        this.pickupLocation = pickupLocation;
        this.returnDate = returnDate;
        this.returnLocation = returnLocation;
        this.isPaid = isPaid;
        this.creditCardNumber = creditCardNumber;
        this.cvv = cvv;
        this.customer = customer;
        this.rentalRates = rentalRates;
    }  

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (carRentalReservationRecordId != null ? carRentalReservationRecordId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the carRentalReservationRecordId fields are not set
        if (!(object instanceof CarRentalReservationRecord)) {
            return false;
        }
        CarRentalReservationRecord other = (CarRentalReservationRecord) object;
        if ((this.carRentalReservationRecordId == null && other.carRentalReservationRecordId != null) || (this.carRentalReservationRecordId != null && !this.carRentalReservationRecordId.equals(other.carRentalReservationRecordId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CarRentalReservationRecord[ id=" + carRentalReservationRecordId + " ]";
    }

    public Date getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(Date pickupDate) {
        this.pickupDate = pickupDate;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public String getReturnLocation() {
        return returnLocation;
    }

    public void setReturnLocation(String returnLocation) {
        this.returnLocation = returnLocation;
    }

    public String getCategoryChoice() {
        return categoryChoice;
    }

    public void setCategoryChoice(String categoryChoice) {
        this.categoryChoice = categoryChoice;
    }

    public String getMakeChoice() {
        return makeChoice;
    }

    public void setMakeChoice(String makeChoice) {
        this.makeChoice = makeChoice;
    }

    public String getModelChoice() {
        return modelChoice;
    }

    public void setModelChoice(String modelChoice) {
        this.modelChoice = modelChoice;
    }

    public boolean isIsCancelled() {
        return isCancelled;
    }

    public void setIsCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public boolean isIsPaid() {
        return isPaid;
    }

    public void setIsPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<RentalRate> getRentalRates() {
        return rentalRates;
    }

    public void setRentalRates(List<RentalRate> rentalRates) {
        this.rentalRates = rentalRates;
    }

    
 
}
