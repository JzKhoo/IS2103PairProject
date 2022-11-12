/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author khoojingzhi
 */
@Entity
public class RentalRate implements Serializable {

    // Attributes
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalRateId;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private String rentalRateType;
    @Column(nullable = false)
    private int ratePerDay;
    private Timestamp startDateTime;
    private Timestamp endDateTime;
    @Column(nullable = false)
    private boolean isDisabled = false;
    
    // Relationships
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private CarCategory carCategory;
    @OneToMany(mappedBy = "rentalRate")
    private List<CarRentalReservationRecord> carRentalReservationRecords;

    public Long getRentalRateId() {
        return rentalRateId;
    }

    public void setRentalRateId(Long rentalRateId) {
        this.rentalRateId = rentalRateId;
    }

    public RentalRate() {
        this.carRentalReservationRecords = new ArrayList<>();
    }

    public RentalRate(String name, String rentalRateType, CarCategory carCategory, int ratePerDay, Timestamp startDateTime, Timestamp endDateTime) {
        this();
        
        this.name = name;
        this.rentalRateType = rentalRateType;
        this.carCategory = carCategory;
        this.ratePerDay = ratePerDay;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    } 

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rentalRateId != null ? rentalRateId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the rentalRateId fields are not set
        if (!(object instanceof RentalRate)) {
            return false;
        }
        RentalRate other = (RentalRate) object;
        if ((this.rentalRateId == null && other.rentalRateId != null) || (this.rentalRateId != null && !this.rentalRateId.equals(other.rentalRateId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RentalRateRecord[ id=" + rentalRateId + " ]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getRentalRateType() {
        return rentalRateType;
    }

    public void setRentalRateType(String rentalRateType) {
        this.rentalRateType = rentalRateType;
    }

    public int getRatePerDay() {
        return ratePerDay;
    }

    public void setRatePerDay(int ratePerDay) {
        this.ratePerDay = ratePerDay;
    }

    public Timestamp getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Timestamp startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Timestamp getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Timestamp endDateTime) {
        this.endDateTime = endDateTime;
    }

    public boolean isIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    public CarCategory getCarCategory() {
        return carCategory;
    }

    public void setCarCategory(CarCategory carCategory) {
        this.carCategory = carCategory;
    }

    public List<CarRentalReservationRecord> getCarRentalReservationRecords() {
        return carRentalReservationRecords;
    }

    public void setCarRentalReservationRecords(List<CarRentalReservationRecord> carRentalReservationRecords) {
        this.carRentalReservationRecords = carRentalReservationRecords;
    }
    
}
