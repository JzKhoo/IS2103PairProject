/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author khoojingzhi
 */
@Entity
public class TransitDriverDispatchRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transitDriverDispatchRecordId;
    @Column(nullable = false)
    private Timestamp transitStartTime;
    @Column(nullable = false)
    private boolean isCompleted;
    
    @ManyToOne
    private Employee employee;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Car car;
    
    

    public Long getTransitDriverDispatchRecordId() {
        return transitDriverDispatchRecordId;
    }

    public void setTransitDriverDispatchRecordId(Long transitDriverDispatchRecordId) {
        this.transitDriverDispatchRecordId = transitDriverDispatchRecordId;
    }

    public TransitDriverDispatchRecord() {
    }

    public TransitDriverDispatchRecord(Timestamp transitStartTime, boolean isCompleted, Car car) {
        this.transitStartTime = transitStartTime;
        this.isCompleted = isCompleted;
        this.car = car;
    }

    public TransitDriverDispatchRecord(Timestamp transitStartTime, boolean isCompleted, Employee employee, Car car) {
        this.transitStartTime = transitStartTime;
        this.isCompleted = isCompleted;
        this.employee = employee;
        this.car = car;
    }

    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transitDriverDispatchRecordId != null ? transitDriverDispatchRecordId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the transitDriverDispatchRecordId fields are not set
        if (!(object instanceof TransitDriverDispatchRecord)) {
            return false;
        }
        TransitDriverDispatchRecord other = (TransitDriverDispatchRecord) object;
        if ((this.transitDriverDispatchRecordId == null && other.transitDriverDispatchRecordId != null) || (this.transitDriverDispatchRecordId != null && !this.transitDriverDispatchRecordId.equals(other.transitDriverDispatchRecordId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TransitDriverDispatchRecord[ id=" + transitDriverDispatchRecordId + " ]";
    }

    public boolean isIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    /**
     * @return the transitStartTime
     */
    public Timestamp getTransitStartTime() {
        return transitStartTime;
    }

    /**
     * @param transitStartTime the transitStartTime to set
     */
    public void setTransitStartTime(Timestamp transitStartTime) {
        this.transitStartTime = transitStartTime;
    }
    
}