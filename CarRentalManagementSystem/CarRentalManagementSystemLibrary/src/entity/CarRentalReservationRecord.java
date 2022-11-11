/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import util.enumeration.RentalFeeOption;

/**
 *
 * @author khoojingzhi
 */
@Entity
public class CarRentalReservationRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carRentalReservationRecordId;
    @Column(nullable = false)
    private Timestamp date;
    @Column(nullable = false)
    private String categoryTypeChoice;
    @Column(nullable = false)
    private String makeChoice;
    @Column(nullable = false)
    private String modelChoice;
    @Column(nullable = false)
    private String pickupLocation;
    @Column(nullable = false)
    private String returnLocation;
    @Column(nullable = false)
    private RentalFeeOption rentalFeeOption;
    @Column(nullable = false)
    private boolean isCancelled;
    
    @ManyToOne
    private Car car;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Customer customer;

    public Long getCarRentalReservationRecordId() {
        return carRentalReservationRecordId;
    }

    public void setCarRentalReservationRecordId(Long carRentalReservationRecordId) {
        this.carRentalReservationRecordId = carRentalReservationRecordId;
    }

    public CarRentalReservationRecord() {
    }

    public CarRentalReservationRecord(Timestamp date, String categoryTypeChoice, String makeChoice, String modelChoice, String pickupLocation, String returnLocation, RentalFeeOption rentalFeeOption, boolean isCancelled, Car car, Customer customer) {
        this.date = date;
        this.categoryTypeChoice = categoryTypeChoice;
        this.makeChoice = makeChoice;
        this.modelChoice = modelChoice;
        this.pickupLocation = pickupLocation;
        this.returnLocation = returnLocation;
        this.rentalFeeOption = rentalFeeOption;
        this.isCancelled = isCancelled;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getCategoryTypeChoice() {
        return categoryTypeChoice;
    }

    public void setCategoryTypeChoice(String categoryTypeChoice) {
        this.categoryTypeChoice = categoryTypeChoice;
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
    
}
