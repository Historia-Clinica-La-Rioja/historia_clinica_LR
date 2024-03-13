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
public class DocumentMeasuringPointPK implements Serializable {

    private static final long serialVersionUID = 215351984761537837L;

    @Column(name = "document_id", nullable = false)
    private Long documentId;

    @Column(name = "measuring_point_id", nullable = false)
    private Integer measuringPointId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DocumentMeasuringPointPK)) return false;

        DocumentMeasuringPointPK that = (DocumentMeasuringPointPK) o;

        if (!getDocumentId().equals(that.getDocumentId())) return false;
        return getMeasuringPointId().equals(that.getMeasuringPointId());
    }

    @Override
    public int hashCode() {
        int result = getDocumentId().hashCode();
        result = 31 * result + getMeasuringPointId().hashCode();
        return result;
    }
}
