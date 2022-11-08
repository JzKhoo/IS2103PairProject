/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
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
    private Long rentalRateRecordId;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private String rentalRateType;
    @Column(nullable = false)
    private double ratePerDay;
    private Date validityStartDate;
    private Date validityEndDate;
    @Column(nullable = false)
    private boolean isDisabled;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private Category category;

    public Long getRentalRateRecordId() {
        return rentalRateRecordId;
    }

    public void setRentalRateRecordId(Long rentalRateRecordId) {
        this.rentalRateRecordId = rentalRateRecordId;
    }

    public RentalRate() {
    }

    public RentalRate(String name, String rentalRateType, double ratePerDay, Date validityStartDate, Date validityEndDate, boolean isDisabled, Category category) {
        this.name = name;
        this.ratePerDay = ratePerDay;
        this.rentalRateType = rentalRateType;
        this.validityStartDate = validityStartDate;
        this.validityEndDate = validityEndDate;
        this.isDisabled = isDisabled;
        this.category = category;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rentalRateRecordId != null ? rentalRateRecordId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the rentalRateRecordId fields are not set
        if (!(object instanceof RentalRate)) {
            return false;
        }
        RentalRate other = (RentalRate) object;
        if ((this.rentalRateRecordId == null && other.rentalRateRecordId != null) || (this.rentalRateRecordId != null && !this.rentalRateRecordId.equals(other.rentalRateRecordId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RentalRateRecord[ id=" + rentalRateRecordId + " ]";
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

    public void setRatePerDay(double ratePerDay) {
        this.ratePerDay = ratePerDay;
    }

    public Date getValidityStartDate() {
        return validityStartDate;
    }

    public void setValidityStartDate(Date validityStartDate) {
        this.validityStartDate = validityStartDate;
    }

    public Date getValidityEndDate() {
        return validityEndDate;
    }

    public void setValidityEndDate(Date validityEndDate) {
        this.validityEndDate = validityEndDate;
    }

    public boolean isIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * @return the rentalRateType
     */
    public String getRentalRateType() {
        return rentalRateType;
    }

    /**
     * @param rentalRateType the rentalRateType to set
     */
    public void setRentalRateType(String rentalRateType) {
        this.rentalRateType = rentalRateType;
    }
    
}
