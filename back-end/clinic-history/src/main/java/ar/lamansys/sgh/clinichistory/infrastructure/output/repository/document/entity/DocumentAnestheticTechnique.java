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
@Table(name = "document_anesthetic_technique")
@Entity
public class DocumentAnestheticTechnique {

    @EmbeddedId
    private DocumentAnestheticTechniquePK documentAnestheticTechniquePK;

    public DocumentAnestheticTechnique(Long documentId, Integer anestheticTechniqueId) {
        documentAnestheticTechniquePK = new DocumentAnestheticTechniquePK(documentId, anestheticTechniqueId);
    }
}
