package net.pladema.clinichistory.requests.transcribed.infrastructure.output.repository.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class TranscribedServiceRequestDiagnosticReportPK implements Serializable {

    @Column(name = "transcribed_service_request_id", nullable = false)
    private Integer transcribedServiceRequestId;

    @Column(name = "diagnostic_report_id", nullable = false)
    private Integer diagnosticReportId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TranscribedServiceRequestDiagnosticReportPK)) return false;

        TranscribedServiceRequestDiagnosticReportPK that = (TranscribedServiceRequestDiagnosticReportPK) o;

        if (!Objects.equals(transcribedServiceRequestId, that.transcribedServiceRequestId))
            return false;
        return Objects.equals(diagnosticReportId, that.diagnosticReportId);
    }

    @Override
    public int hashCode() {
        int result = transcribedServiceRequestId != null ? transcribedServiceRequestId.hashCode() : 0;
        result = 31 * result + (diagnosticReportId != null ? diagnosticReportId.hashCode() : 0);
        return result;
    }
}
