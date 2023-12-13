package ar.lamansys.sgh.clinichistory.domain.hce;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity.HCEHealthConditionVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionClinicalStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionVerificationStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ProblemType;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class HCEHealthConditionBo extends HCEClinicalTermBo {

    private String verificationId;

    private String verification;

    private String problemId;

    private String severity;

    private LocalDate startDate;

    private LocalDate inactivationDate;

    private boolean main;

    private String institutionName;

    private Integer professionalPersonId;

    private String professionalName;

    private LocalDateTime createdOn;

    private String note;

    private boolean hasPendingReference;

    private Boolean canBeMarkAsError;

    public HCEHealthConditionBo(HCEHealthConditionVo source) {
        super(source.getId(), source.getSnomed(), source.getStatusId(), source.getStatus(), source.getPatientId());
        this.verificationId = source.getVerificationId();
        this.verification = source.getVerification();
        this.problemId = source.getProblemId();
        this.severity = source.getSeverity();
        this.startDate = source.getStartDate();
        this.inactivationDate = source.getInactivationDate();
        this.main = source.isMain();
        this.institutionName = source.getInstitutionName();
        this.professionalPersonId = source.getProfessionalUserId();
        this.createdOn = source.getCreatedOn();
        this.note = source.getNote();
        this.canBeMarkAsError = !isSolvedProblem();
    }

    public boolean isChronic() {
        return getStatusId().equals(ConditionClinicalStatus.ACTIVE) && problemId.equals(ProblemType.CHRONIC);
    }

    public boolean isActiveProblem() {
        return getStatusId().equals(ConditionClinicalStatus.ACTIVE) && !problemId.equals(ProblemType.CHRONIC);
    }

    public boolean isSolvedProblem() {
        return getStatusId().equals(ConditionClinicalStatus.SOLVED);
    }

    public boolean isMarkedAsError() {
        return getVerificationId().equalsIgnoreCase(ConditionVerificationStatus.ERROR);
    }
}
