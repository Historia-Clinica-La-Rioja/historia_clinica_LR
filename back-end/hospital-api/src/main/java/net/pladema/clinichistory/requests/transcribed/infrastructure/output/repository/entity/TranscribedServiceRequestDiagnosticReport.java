package net.pladema.clinichistory.requests.transcribed.infrastructure.output.repository.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
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
@Table(name = "transcribed_service_request_diagnostic_report")
@Entity
public class TranscribedServiceRequestDiagnosticReport {

    @EmbeddedId
    TranscribedServiceRequestDiagnosticReportPK pk;

    public TranscribedServiceRequestDiagnosticReport(Integer transcribedServiceRequestId, Integer diagnosticReportId) {
        this.pk = new TranscribedServiceRequestDiagnosticReportPK(transcribedServiceRequestId, diagnosticReportId);
    }

}
