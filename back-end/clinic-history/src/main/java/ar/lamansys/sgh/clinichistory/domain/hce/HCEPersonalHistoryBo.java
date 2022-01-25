package ar.lamansys.sgh.clinichistory.domain.hce;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionClinicalStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ProblemType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity.HCEHealthConditionVo;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class HCEPersonalHistoryBo extends HCEClinicalTermBo {

    private String verificationId;

    private String verification;

    private String problemId;

    private String severity;

    private LocalDate startDate;

    private LocalDate inactivationDate;

    private boolean main;

    private boolean hasPendingReference;

    public HCEPersonalHistoryBo(HCEHealthConditionVo source){
        super(source.getId(), source.getSnomed(), source.getStatusId(), source.getStatus(), source.getPatientId());
        this.verificationId = source.getVerificationId();
        this.verification = source.getVerification();
        this.problemId = source.getProblemId();
        this.severity = source.getSeverity();
        this.startDate = source.getStartDate();
        this.inactivationDate = source.getInactivationDate();
        this.main = source.isMain();
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
}
