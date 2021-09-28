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
public class ToothIndicesPK implements Serializable {

    @Column(name = "patient_id", nullable = false)
    private Integer patientId;

    @Column(name = "tooth_id", nullable = false, length = 20)
    private String toothId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ToothIndicesPK that = (ToothIndicesPK) o;
        return patientId.equals(that.patientId) &&
                toothId.equals(that.toothId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patientId, toothId);
    }

}
