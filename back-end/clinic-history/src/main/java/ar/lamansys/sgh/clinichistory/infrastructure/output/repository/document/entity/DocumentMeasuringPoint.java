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
@Table(name = "document_measuring_point")
@Entity
public class DocumentMeasuringPoint {

    @EmbeddedId
    private DocumentMeasuringPointPK pk;

    public DocumentMeasuringPoint(Long documentId, Integer measuringPointId) {
        pk = new DocumentMeasuringPointPK(documentId, measuringPointId);
    }
}
