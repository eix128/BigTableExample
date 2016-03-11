package jpa;

import javax.persistence.*;

/**
 * Created by kadir.basol on 25.2.2016.
 */
@Entity
@Table(name = "customer", schema = "depo", catalog = "")
public class CustomerEntity {
    private int id;
    private String name;
    private String surname;
    private String unvan;
    private CustumerType type;
    private String phoneNumber;
    private String address;
    private Long tcKimlik;
    private String vergiKimlik;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = true, length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "surname", nullable = true, length = 255)
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Basic
    @Column(name = "unvan", nullable = true, length = 255)
    public String getUnvan() {
        return unvan;
    }

    public void setUnvan(String unvan) {
        this.unvan = unvan;
    }

    @Basic
    @Column(name = "type", nullable = true)
    public CustumerType getType() {
        return type;
    }

    public void setType(CustumerType type) {
        this.type = type;
    }

    @Basic
    @Column(name = "phoneNumber", nullable = true, length = 255)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Basic
    @Column(name = "address", nullable = true, length = 255)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @Column(name = "tcKimlik", nullable = true)
    public Long getTcKimlik() {
        return tcKimlik;
    }

    public void setTcKimlik(Long tcKimlik) {
        this.tcKimlik = tcKimlik;
    }

    @Basic
    @Column(name = "vergiKimlik", nullable = true, length = 255)
    public String getVergiKimlik() {
        return vergiKimlik;
    }

    public void setVergiKimlik(String vergiKimlik) {
        this.vergiKimlik = vergiKimlik;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomerEntity that = (CustomerEntity) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (surname != null ? !surname.equals(that.surname) : that.surname != null) return false;
        if (unvan != null ? !unvan.equals(that.unvan) : that.unvan != null) return false;
        if (type != that.type) return false;
        if (phoneNumber != null ? !phoneNumber.equals(that.phoneNumber) : that.phoneNumber != null) return false;
        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        if (tcKimlik != null ? !tcKimlik.equals(that.tcKimlik) : that.tcKimlik != null) return false;
        if (vergiKimlik != null ? !vergiKimlik.equals(that.vergiKimlik) : that.vergiKimlik != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (unvan != null ? unvan.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (tcKimlik != null ? tcKimlik.hashCode() : 0);
        result = 31 * result + (vergiKimlik != null ? vergiKimlik.hashCode() : 0);
        return result;
    }
}
