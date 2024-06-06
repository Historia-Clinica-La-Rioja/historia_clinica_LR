package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Embeddable
public class TrachealIntubationPK implements Serializable {

    @Column(name = "anesthetic_technique_id", nullable = false)
    private Integer anestheticTechniqueId;

    @Column(name = "tracheal_intubation_id", nullable = false)
    private Short trachealIntubationId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrachealIntubationPK)) return false;

        TrachealIntubationPK that = (TrachealIntubationPK) o;

        if (!getAnestheticTechniqueId().equals(that.getAnestheticTechniqueId())) return false;
        return getTrachealIntubationId().equals(that.getTrachealIntubationId());
    }

    @Override
    public int hashCode() {
        int result = getAnestheticTechniqueId().hashCode();
        result = 31 * result + getTrachealIntubationId().hashCode();
        return result;
    }
}
