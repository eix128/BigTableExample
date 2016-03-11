package jpa;

import javax.persistence.*;

/**
 * Created by kadir.basol on 25.2.2016.
 */
@Entity
@Table(name = "process", schema = "depo", catalog = "")
public class ProcessEntity {
    private int id;
    private Integer productId;
    private Integer depotId;
    private ProcessType processType;
    private Integer companyId;
    private Integer processAmount;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "productId", nullable = true)
    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
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
    @Column(name = "processType", nullable = true)
    public ProcessType getProcessType() {
        return processType;
    }

    public void setProcessType(ProcessType processType) {
        this.processType = processType;
    }

    @Basic
    @Column(name = "companyId", nullable = true)
    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    @Basic
    @Column(name = "processAmount", nullable = true)
    public Integer getProcessAmount() {
        return processAmount;
    }

    public void setProcessAmount(Integer processAmount) {
        this.processAmount = processAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProcessEntity that = (ProcessEntity) o;

        if (id != that.id) return false;
        if (productId != null ? !productId.equals(that.productId) : that.productId != null) return false;
        if (depotId != null ? !depotId.equals(that.depotId) : that.depotId != null) return false;
        if (processType != that.processType) return false;
        if (companyId != null ? !companyId.equals(that.companyId) : that.companyId != null) return false;
        if (processAmount != null ? !processAmount.equals(that.processAmount) : that.processAmount != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (productId != null ? productId.hashCode() : 0);
        result = 31 * result + (depotId != null ? depotId.hashCode() : 0);
        result = 31 * result + (processType != null ? processType.hashCode() : 0);
        result = 31 * result + (companyId != null ? companyId.hashCode() : 0);
        result = 31 * result + (processAmount != null ? processAmount.hashCode() : 0);
        return result;
    }
}
