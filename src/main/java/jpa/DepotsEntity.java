package jpa;

import javax.persistence.*;

/**
 * Created by kadir.basol on 25.2.2016.
 */
@Entity
@Table(name = "depots", schema = "dbo")
public class DepotsEntity {

    private int id;
    private Integer depotId;
    private String depotName;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "depotId", nullable = true)
    public Integer getDepotId() {
        return depotId;
    }

    public void setDepotId(Integer depotId) {
        this.depotId = depotId;
    }

    @Basic
    @Column(name = "depotName", nullable = true, length = 255)
    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DepotsEntity that = (DepotsEntity) o;

        if (id != that.id) return false;
        if (depotId != null ? !depotId.equals(that.depotId) : that.depotId != null) return false;
        if (depotName != null ? !depotName.equals(that.depotName) : that.depotName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (depotId != null ? depotId.hashCode() : 0);
        result = 31 * result + (depotName != null ? depotName.hashCode() : 0);
        return result;
    }
}
