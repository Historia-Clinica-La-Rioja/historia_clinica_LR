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
public class DocumentAnestheticTechniquePK implements Serializable {

    @Column(name = "document_id", nullable = false)
    private Long documentId;

    @Column(name = "anesthetic_technique_id", nullable = false)
    private Integer anestheticTechniqueId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DocumentAnestheticTechniquePK)) return false;

        DocumentAnestheticTechniquePK that = (DocumentAnestheticTechniquePK) o;

        if (!getDocumentId().equals(that.getDocumentId())) return false;
        return getAnestheticTechniqueId().equals(that.getAnestheticTechniqueId());
    }

    @Override
    public int hashCode() {
        int result = getDocumentId().hashCode();
        result = 31 * result + getAnestheticTechniqueId().hashCode();
        return result;
    }
}