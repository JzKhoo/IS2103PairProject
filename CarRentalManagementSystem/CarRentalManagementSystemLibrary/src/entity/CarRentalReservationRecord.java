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
import util.enumeration.RentalFeeOption;

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
    private String pickupLocation;
    @Column(nullable = false)
    private String returnLocation;
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date pickUpDate;
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date returnDate;
    @Column(nullable = false)
    private RentalFeeOption rentalFeeOption;
    @Column(nullable = false)
    private boolean isCancelled = false;
    
    // Relationships
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
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

    public CarRentalReservationRecord(String pickupLocation, String returnLocation, Date pickUpDate, Date returnDate, RentalFeeOption rentalFeeOption, Car car, Customer customer) {
        this();
        
        this.pickupLocation = pickupLocation;
        this.returnLocation = returnLocation;
        this.pickUpDate = pickUpDate;
        this.returnDate = returnDate;
        this.rentalFeeOption = rentalFeeOption;
        this.car = car;
        this.customer = customer;
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

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getReturnLocation() {
        return returnLocation;
    }

    public void setReturnLocation(String returnLocation) {
        this.returnLocation = returnLocation;
    }

    public Date getPickUpDate() {
        return pickUpDate;
    }

    public void setPickUpDate(Date pickUpDate) {
        this.pickUpDate = pickUpDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public RentalFeeOption getRentalFeeOption() {
        return rentalFeeOption;
    }

    public void setRentalFeeOption(RentalFeeOption rentalFeeOption) {
        this.rentalFeeOption = rentalFeeOption;
    }

    public boolean isIsCancelled() {
        return isCancelled;
    }

    public void setIsCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
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
