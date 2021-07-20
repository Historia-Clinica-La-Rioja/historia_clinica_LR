package ar.lamansys.odontology.infrastructure.repository.consultation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Embeddable
public class OdontologyConsultationReasonPK implements Serializable {

    @Column(name = "odontology_consultation_id", nullable = false)
    private Integer odontologyConsultationId;

    @Column(name = "reason_id", nullable = false)
    private String reasonId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OdontologyConsultationReasonPK that = (OdontologyConsultationReasonPK) o;
        return odontologyConsultationId.equals(that.odontologyConsultationId) &&
                reasonId.equals(that.reasonId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(odontologyConsultationId, reasonId);
    }

}
