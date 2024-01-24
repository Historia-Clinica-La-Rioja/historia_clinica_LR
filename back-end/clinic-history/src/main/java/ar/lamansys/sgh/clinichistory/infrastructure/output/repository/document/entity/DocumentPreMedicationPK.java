package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity;

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
public class DocumentPreMedicationPK implements Serializable {

    @Column(name = "document_id", nullable = false)
    private Long documentId;

    @Column(name = "pre_medication_id", nullable = false)
    private Integer preMedicationId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DocumentPreMedicationPK)) return false;

        DocumentPreMedicationPK that = (DocumentPreMedicationPK) o;

        if (!getDocumentId().equals(that.getDocumentId())) return false;
        return getPreMedicationId().equals(that.getPreMedicationId());
    }

    @Override
    public int hashCode() {
        int result = getDocumentId().hashCode();
        result = 31 * result + getPreMedicationId().hashCode();
        return result;
    }
}
