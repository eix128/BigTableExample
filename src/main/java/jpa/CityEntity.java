package jpa;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by kadir.basol on 2.3.2016.
 */
@Entity
@Table(name = "city", schema = "dbo", catalog = "")
@NamedQueries(
        @NamedQuery(name = "CityEntity.getAll", query = "Select p from CityEntity p")
)
public class CityEntity {
    private short cityId;
    private String city;
    private Timestamp lastUpdate;

    @Id
    @Column(name = "city_id", nullable = false)
    public short getCityId() {
        return cityId;
    }

    public void setCityId(short cityId) {
        this.cityId = cityId;
    }

    @Basic
    @Column(name = "city", nullable = false, length = 50)
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Basic
    @Column(name = "last_update", nullable = false)
    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CityEntity that = (CityEntity) o;

        if (cityId != that.cityId) return false;
        if (city != null ? !city.equals(that.city) : that.city != null) return false;
        if (lastUpdate != null ? !lastUpdate.equals(that.lastUpdate) : that.lastUpdate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) cityId;
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (lastUpdate != null ? lastUpdate.hashCode() : 0);
        return result;
    }
}
