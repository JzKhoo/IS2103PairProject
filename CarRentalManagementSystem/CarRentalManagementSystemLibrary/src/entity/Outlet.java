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
import javax.persistence.OneToMany;

/**
 *
 * @author khoojingzhi
 */
@Entity
public class Outlet implements Serializable {

    // Attributes
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long outletId;
    @Column(nullable = false, unique = true)
    private String name;
    private String openingHour;
    private String closingHour;
    private String address;
    
    // Relationships
    @OneToMany(mappedBy = "outlet")
    private List<Car> cars;
    @OneToMany(mappedBy = "outlet")
    private List<Employee> employees;

    
    public Long getOutletId() {
        return outletId;
    }

    public void setOutletId(Long outletId) {
        this.outletId = outletId;
    }

    // Constructors
    public Outlet() {
        this.cars = new ArrayList<>();
        this.employees = new ArrayList<>();
    }

    public Outlet(String name, String openingHour, String closingHour) {
        this();
        
        this.name = name;
        this.openingHour = openingHour;
        this.closingHour = closingHour;
    }
    
    public Outlet(String name, String openingHour, String closingHour, String address) {
        this();
        
        this.name = name;
        this.openingHour = openingHour;
        this.closingHour = closingHour;
        this.address = address;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (outletId != null ? outletId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the outletId fields are not set
        if (!(object instanceof Outlet)) {
            return false;
        }
        Outlet other = (Outlet) object;
        if ((this.outletId == null && other.outletId != null) || (this.outletId != null && !this.outletId.equals(other.outletId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Outlet[ id=" + outletId + " ]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpeningHour() {
        return openingHour;
    }

    public void setOpeningHour(String openingHour) {
        this.openingHour = openingHour;
    }

    public String getClosingHour() {
        return closingHour;
    }

    public void setClosingHour(String closingHour) {
        this.closingHour = closingHour;
    }
    
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

}
