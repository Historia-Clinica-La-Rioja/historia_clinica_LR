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
@Table(name = "document_odontology_procedure")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class DocumentOdontologyProcedure {

    @EmbeddedId
    private DocumentOdontologyProcedurePK pk;

    public DocumentOdontologyProcedure(Long documentId, Integer odontologyProcedureId){
        this.pk = new DocumentOdontologyProcedurePK(documentId, odontologyProcedureId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentOdontologyProcedure that = (DocumentOdontologyProcedure) o;
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
