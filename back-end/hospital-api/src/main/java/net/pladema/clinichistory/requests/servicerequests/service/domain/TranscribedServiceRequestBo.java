package net.pladema.clinichistory.requests.servicerequests.service.domain;

import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import net.pladema.clinichistory.requests.servicerequests.domain.IServiceRequestBo;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TranscribedServiceRequestBo implements IServiceRequestBo {

    private Integer id;

    private PatientInfoBo patientInfo;

    private HealthConditionBo healthCondition;

    private List<DiagnosticReportBo> diagnosticReports;

    private String healthcareProfessionalName;

    private String institutionName;

    private String observations;

    private LocalDateTime creationDate;

    public TranscribedServiceRequestBo(Integer id, Integer patientId, String healthcareProfessionalName, String institutionName, LocalDateTime creationDate, String observations) {
        this.id = id;
        this.patientInfo = new PatientInfoBo(patientId);
        this.healthcareProfessionalName = healthcareProfessionalName;
        this.institutionName = institutionName;
        this.creationDate = creationDate;
        this.observations = observations;
    }

    @Override
    public Integer getServiceRequestId() {
        return id;
    }

    @Override
    public LocalDate getReportDate() {
        return creationDate.toLocalDate();
    }

    @Override
    public List<String> getStudies() {
        return diagnosticReports.stream()
                .map(DiagnosticReportBo::getDiagnosticReportSnomedPt)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getProblemsPt() {
        return List.of(healthCondition != null && healthCondition.getSnomed() != null ? healthCondition.getSnomedPt() : diagnosticReports.get(0).getSnomedPt());
    }

    @Override
    public List<String> getCie10Codes() {
        return List.of(healthCondition != null ? healthCondition.getCie10codes() : diagnosticReports.get(0).getHealthCondition().getCie10codes());
    }

    @Override
    public Short getAssociatedSourceTypeId() {
        return SourceType.OUTPATIENT;
    }

    public boolean isTranscribed() {
        return true;
    }

    public Integer getPatientId() {
        if (patientInfo != null)
            return patientInfo.getId();
        return null;
    }

}

