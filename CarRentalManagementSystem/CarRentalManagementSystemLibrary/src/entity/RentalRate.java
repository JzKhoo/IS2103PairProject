/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author khoojingzhi
 */
@Entity
public class RentalRate implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalRateId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private int ratePerDay;
    private Timestamp startDateTime;
    private Timestamp startEndTime;
    @Column(nullable = false)
    private boolean isDisabled = false;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private CarCategory carCategory;

    public Long getRentalRateId() {
        return rentalRateId;
    }

    public void setRentalRateId(Long rentalRateId) {
        this.rentalRateId = rentalRateId;
    }

    public RentalRate() {
    }

    public RentalRate(String name, CarCategory carCategory, int ratePerDay, Timestamp startDateTime, Timestamp startEndTime) {
        this.name = name;
        this.carCategory = carCategory;
        this.ratePerDay = ratePerDay;
        this.startDateTime = startDateTime;
        this.startEndTime = startEndTime;
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

    public double getRatePerDay() {
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

    public Timestamp getStartEndTime() {
        return startEndTime;
    }

    public void setStartEndTime(Timestamp startEndTime) {
        this.startEndTime = startEndTime;
    }

    public boolean isIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    public CarCategory getCategory() {
        return carCategory;
    }

    public void setCategory(CarCategory carCategory) {
        this.carCategory = carCategory;
    }
    
}