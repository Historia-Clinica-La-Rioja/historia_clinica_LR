package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "document_odontology_diagnostic")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class DocumentOdontologyDiagnostic {

    @EmbeddedId
    private DocumentOdontologyDiagnosticPK pk;

    public DocumentOdontologyDiagnostic(Long documentId, Integer odontologyProcedureId){
        this.pk = new DocumentOdontologyDiagnosticPK(documentId, odontologyProcedureId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentOdontologyDiagnostic that = (DocumentOdontologyDiagnostic) o;
        return Objects.equals(pk, that.pk);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pk);
    }

    public Long getDocumentId() {
        return pk.getDocumentId();
    }

}
