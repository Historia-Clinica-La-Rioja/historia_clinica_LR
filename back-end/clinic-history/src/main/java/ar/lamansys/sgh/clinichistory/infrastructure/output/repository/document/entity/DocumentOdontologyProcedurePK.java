package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity;

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
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class DocumentOdontologyProcedurePK implements Serializable {

    @Column(name = "document_id", nullable = false)
    private Long documentId;

    @Column(name = "odontology_procedure_id", nullable = false)
    private Integer odontologyProcedureId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentOdontologyProcedurePK that = (DocumentOdontologyProcedurePK) o;
        return documentId.equals(that.documentId) &&
                odontologyProcedureId.equals(that.odontologyProcedureId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(documentId, odontologyProcedureId);
    }

}
