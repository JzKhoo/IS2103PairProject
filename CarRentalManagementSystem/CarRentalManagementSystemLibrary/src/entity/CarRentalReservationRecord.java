/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.sql.Timestamp;
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
import javax.persistence.OneToOne;
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
    private String categoryTypeChoice;
    private String modelChoice;
    @Column(nullable = false)
    private String pickupLocation;
    @Column(nullable = false)
    private String returnLocation;
    @Column(nullable = false)
    private RentalFeeOption rentalFeeOption;
    @Column(nullable = false)
    private boolean isCancelled;
    @Column(nullable = false)
    private Timestamp pickupTime;
    @Column(nullable = false)
    private Timestamp returnTime;
    private String reservationDetails;
    
    @ManyToOne
    private Car car;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Customer customer;
    @ManyToMany(mappedBy = "carRentalReservationRecords")
    private List<RentalRate> rentalRates;

    public Long getCarRentalReservationRecordId() {
        return carRentalReservationRecordId;
    }

    public void setCarRentalReservationRecordId(Long carRentalReservationRecordId) {
        this.carRentalReservationRecordId = carRentalReservationRecordId;
        this.setRentalRates(new ArrayList<RentalRate>());
    }

    public CarRentalReservationRecord() {
    }

    public CarRentalReservationRecord(String categoryTypeChoice, String modelChoice, String pickupLocation, String returnLocation, RentalFeeOption rentalFeeOption, Timestamp pickupTime, Timestamp returnTime, String reservationDetails, Customer customer) {
        this.categoryTypeChoice = categoryTypeChoice;
        this.modelChoice = modelChoice;
        this.pickupLocation = pickupLocation;
        this.returnLocation = returnLocation;
        this.rentalFeeOption = rentalFeeOption;
        this.isCancelled = false;
        this.pickupTime = pickupTime;
        this.returnTime = returnTime;
        this.reservationDetails = reservationDetails;
        this.customer = customer;
        this.rentalRates = new ArrayList<RentalRate>();
    }
    


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getCarRentalReservationRecordId() != null ? getCarRentalReservationRecordId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the carRentalReservationRecordId fields are not set
        if (!(object instanceof CarRentalReservationRecord)) {
            return false;
        }
        CarRentalReservationRecord other = (CarRentalReservationRecord) object;
        if ((this.getCarRentalReservationRecordId() == null && other.getCarRentalReservationRecordId() != null) || (this.getCarRentalReservationRecordId() != null && !this.carRentalReservationRecordId.equals(other.carRentalReservationRecordId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CarRentalReservationRecord[ id=" + getCarRentalReservationRecordId() + " ]";
    }
    
    public Long setcarRentalReservationRecordId()
    {
        return carRentalReservationRecordId;
    }
            
    public String getCategoryTypeChoice() {
        return categoryTypeChoice;
    }

    public void setCategoryTypeChoice(String categoryTypeChoice) {
        this.categoryTypeChoice = categoryTypeChoice;
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

    public List<RentalRate> getRentalRates() {
        return rentalRates;
    }

    public void setRentalRate(List<RentalRate> rentalRates) {
        this.setRentalRates(rentalRates);
    }

    /**
     * @return the pickupTime
     */
    public Timestamp getPickupTime() {
        return pickupTime;
    }

    /**
     * @param pickupTime the pickupTime to set
     */
    public void setPickupTime(Timestamp pickupTime) {
        this.pickupTime = pickupTime;
    }

    /**
     * @return the returnTime
     */
    public Timestamp getReturnTime() {
        return returnTime;
    }

    /**
     * @param returnTime the returnTime to set
     */
    public void setReturnTime(Timestamp returnTime) {
        this.returnTime = returnTime;
    }

    /**
     * @return the reservationDetails
     */
    public String getReservationDetails() {
        return reservationDetails;
    }

    /**
     * @param reservationDetails the reservationDetails to set
     */
    public void setReservationDetails(String reservationDetails) {
        this.reservationDetails = reservationDetails;
    }

    /**
     * @param rentalRates the rentalRates to set
     */
    public void setRentalRates(List<RentalRate> rentalRates) {
        this.rentalRates = rentalRates;
    }
    
}