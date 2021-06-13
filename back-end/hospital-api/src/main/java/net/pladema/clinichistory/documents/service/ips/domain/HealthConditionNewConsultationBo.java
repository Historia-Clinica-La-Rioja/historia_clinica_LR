package net.pladema.clinichistory.documents.service.ips.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.documents.repository.ips.entity.HealthCondition;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.ConditionClinicalStatus;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.ProblemType;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class HealthConditionNewConsultationBo extends ClinicalTerm{

    private Integer patientId;

    private Integer snomedId;

    private String verificationStatusId;

    private LocalDate startDate;

    private LocalDate inactivationDate;

    private Boolean main;

    private Long noteId;

    private String problemId;

    private String severity;

    private Boolean isChronic;

    public HealthConditionNewConsultationBo(HealthCondition hc) {
        super();
        this.patientId = hc.getPatientId();
        this.snomedId = hc.getSnomedId();
        this.verificationStatusId = hc.getVerificationStatusId();
        this.startDate = hc.getStartDate();
        this.inactivationDate = hc.getInactivationDate();
        this.main = hc.getMain();
        this.noteId = hc.getNoteId();
        this.problemId = hc.getProblemId();
        this.setStatusId(hc.getStatusId());
        this.severity = hc.getSeverity();
        this.setIsChronic(ProblemType.CHRONIC.equals(hc.getProblemId()));
    }

    public boolean isActive() {
        return getStatusId().equals(ConditionClinicalStatus.ACTIVE);
    }
}
