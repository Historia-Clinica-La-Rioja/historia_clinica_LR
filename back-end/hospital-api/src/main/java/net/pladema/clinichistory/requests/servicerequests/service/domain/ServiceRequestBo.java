package net.pladema.clinichistory.requests.servicerequests.service.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.util.Strings;

import ar.lamansys.sgh.clinichistory.domain.document.IDocumentBo;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.requests.servicerequests.domain.IServiceRequestBo;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ServiceRequestBo implements IDocumentBo, IServiceRequestBo {

    private Long id;

    private Integer serviceRequestId;

    private PatientInfoBo patientInfo;

    private String categoryId;

    private Integer medicalCoverageId;

    private Integer encounterId;

    private Integer institutionId;

    private Integer doctorId;

    private Long noteId;

    private List<DiagnosticReportBo> diagnosticReports;

    private LocalDateTime requestDate = LocalDateTime.now();

	private Short associatedSourceTypeId = SourceType.OUTPATIENT;

	private Integer associatedSourceId;

	private String observations;

    private Map<String, Object> contextMap;

	@Override
    public Integer getPatientId() {
        if (patientInfo != null)
            return patientInfo.getId();
        return null;
    }

    @Override
    public short getDocumentType() {
        return DocumentType.ORDER;
    }

    @Override
    public Short getDocumentSource() {
        return SourceType.ORDER;
    }

    @Override
    public LocalDate getReportDate() {
        return requestDate.toLocalDate();
    }

    @Override
    public List<String> getStudies() {
        return getDiagnosticReports().stream()
                .map(DiagnosticReportBo::getDiagnosticReportSnomedPt)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getProblemsPt() {
        return List.of(getDiagnosticReports().get(0).getHealthCondition().getSnomedPt());
    }

    @Override
    public List<String> getCie10Codes() {
		String cie10Codes = getDiagnosticReports()
				.stream()
				.findFirst()
				.map(DiagnosticReportBo::getHealthCondition)
				.map(HealthConditionBo::getCie10codes)
				.orElse(Strings.EMPTY);
        return List.of(cie10Codes);
    }

    @Override
    public boolean isTranscribed() {
        return false;
    }
}
