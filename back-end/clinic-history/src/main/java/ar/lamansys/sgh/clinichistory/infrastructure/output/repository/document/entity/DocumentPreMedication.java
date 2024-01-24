package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name = "document_pre_medication")
@Entity
public class DocumentPreMedication {

    @EmbeddedId
    private DocumentPreMedicationPK documentPreMedicationPK;

    public DocumentPreMedication(Long documentId, Integer preMedicationId) {
        documentPreMedicationPK = new DocumentPreMedicationPK(documentId, preMedicationId);
    }

}
