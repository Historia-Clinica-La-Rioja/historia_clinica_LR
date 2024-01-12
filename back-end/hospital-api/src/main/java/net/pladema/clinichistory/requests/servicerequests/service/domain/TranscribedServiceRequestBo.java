package net.pladema.clinichistory.requests.servicerequests.service.domain;

import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.requests.controller.dto.TranscribedPrescriptionDto;

import net.pladema.clinichistory.requests.servicerequests.domain.IServiceRequestBo;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class TranscribedServiceRequestBo implements IServiceRequestBo {

    private Integer transcribedServiceRequestId;

    private Integer patientId;

    private SnomedBo healthCondition;

    private String cie10Codes;

    private SnomedBo study;

    private String healthcareProfessionalName;

    private String institutionName;

    private String observations;

    private LocalDateTime creationDate;

    public TranscribedServiceRequestBo(Integer transcribedServiceRequestId, Integer patientId, String healthConditionSctid, String healthConditionPt, String cie10Codes, String studySctid, String studyPt,
                                       String healthcareProfessionalName, String institutionName,
                                       LocalDateTime creationDate) {
        this.transcribedServiceRequestId = transcribedServiceRequestId;
        this.patientId = patientId;
        this.healthCondition = new SnomedBo(healthConditionSctid, healthConditionPt);
        this.cie10Codes = cie10Codes;
        this.study = new SnomedBo(studySctid, studyPt);
        this.healthcareProfessionalName = healthcareProfessionalName;
        this.institutionName = institutionName;
        this.creationDate = creationDate;
    }

    public TranscribedServiceRequestBo(TranscribedPrescriptionDto transcribedPrescriptionDto, Integer patientId) {
        this.patientId = patientId;
        this.healthCondition = new SnomedBo(transcribedPrescriptionDto.getHealthCondition().getSctid(), transcribedPrescriptionDto.getHealthCondition().getPt());
        this.study = new SnomedBo(transcribedPrescriptionDto.getStudy().getSctid(), transcribedPrescriptionDto.getStudy().getPt());
        this.healthcareProfessionalName = transcribedPrescriptionDto.getHealthcareProfessionalName();
        this.institutionName = transcribedPrescriptionDto.getInstitutionName();
        this.observations = transcribedPrescriptionDto.getObservations();
    }

    @Override
    public Integer getServiceRequestId() {
        return transcribedServiceRequestId;
    }

    @Override
    public LocalDate getReportDate() {
        return creationDate.toLocalDate();
    }

    @Override
    public List<String> getProblemsPt() {
        return List.of(healthCondition.getPt());
    }

    @Override
    public List<String> getStudies() {
        return List.of(study.getPt());
    }

    @Override
    public List<String> getCie10Codes() {
        return List.of(cie10Codes);
    }

    @Override
    public Short getAssociatedSourceTypeId() {
        return SourceType.OUTPATIENT;
    }
}
