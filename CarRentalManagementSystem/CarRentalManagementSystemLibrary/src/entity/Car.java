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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author khoojingzhi
 */
@Entity
public class Car implements Serializable {

    // Attributes
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carId;
    @Column(nullable = false, unique = true)
    private String licensePlateNumber;
    @Column(nullable = false)
    private String status;
    @Column(nullable = false)
    private String location;
    @Column(nullable = false)
    private boolean isDisabled = false;
    
    // Relationships
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Outlet outlet;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Model model;
    
    @OneToMany(mappedBy = "car")
    private List<TransitDriverDispatchRecord> transitDriverDispatchRecords;
    
    @OneToMany(mappedBy = "car")
    private List<CarRentalReservationRecord> carRentalReservationRecords; 

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    // Constructors
    public Car() {
        this.transitDriverDispatchRecords = new ArrayList<>();
        this.carRentalReservationRecords = new ArrayList<>();
    }

    public Car(String licensePlateNumber, String status, String location, Outlet outlet, Model model) {
        this();
        
        this.licensePlateNumber = licensePlateNumber;
        this.status = status;
        this.location = location;
        this.outlet = outlet;
        this.model = model;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (carId != null ? carId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the carId fields are not set
        if (!(object instanceof Car)) {
            return false;
        }
        Car other = (Car) object;
        if ((this.carId == null && other.carId != null) || (this.carId != null && !this.carId.equals(other.carId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Car[ id=" + carId + " ]";
    }

    public String getLicensePlateNumber() {
        return licensePlateNumber;
    }

    public void setLicensePlateNumber(String licensePlateNumber) {
        this.licensePlateNumber = licensePlateNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    
    public boolean isIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    public Outlet getOutlet() {
        return outlet;
    }

    public void setOutlet(Outlet outlet) {
        this.outlet = outlet;
    }
    
    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public List<TransitDriverDispatchRecord> getTransitDriverDispatchRecords() {
        return transitDriverDispatchRecords;
    }

    public void setTransitDriverDispatchRecords(List<TransitDriverDispatchRecord> transitDriverDispatchRecords) {
        this.transitDriverDispatchRecords = transitDriverDispatchRecords;
    }

    public List<CarRentalReservationRecord> getCarRentalReservationRecords() {
        return carRentalReservationRecords;
    }

    public void setCarRentalReservationRecords(List<CarRentalReservationRecord> carRentalReservationRecords) {
        this.carRentalReservationRecords = carRentalReservationRecords;
    }
  
}
