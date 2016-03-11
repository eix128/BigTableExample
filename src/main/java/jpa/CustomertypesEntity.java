package jpa;

import javax.persistence.*;

/**
 * Created by kadir.basol on 25.2.2016.
 */
@Entity
@Table(name = "customertypes", schema = "depo", catalog = "")
public class CustomertypesEntity {
    private int id;
    private Integer customerType;
    private String info;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "customerType", nullable = true)
    public Integer getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Integer customerType) {
        this.customerType = customerType;
    }

    @Basic
    @Column(name = "info", nullable = true, length = 255)
    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomertypesEntity that = (CustomertypesEntity) o;

        if (id != that.id) return false;
        if (customerType != null ? !customerType.equals(that.customerType) : that.customerType != null) return false;
        if (info != null ? !info.equals(that.info) : that.info != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (customerType != null ? customerType.hashCode() : 0);
        result = 31 * result + (info != null ? info.hashCode() : 0);
        return result;
    }
}
