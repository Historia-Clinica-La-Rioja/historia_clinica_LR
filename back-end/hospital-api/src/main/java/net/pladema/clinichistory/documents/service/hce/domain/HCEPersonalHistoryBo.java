package net.pladema.clinichistory.documents.service.hce.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.documents.repository.hce.domain.HCEHealthConditionVo;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.ConditionClinicalStatus;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.ProblemType;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class HCEPersonalHistoryBo extends HCEClinicalTermBo {

    private String verificationId;

    private String verification;

    private String problemId;

    private LocalDate startDate;

    private LocalDate inactivationDate;

    private boolean main;

    public HCEPersonalHistoryBo(HCEHealthConditionVo source){
        super(source.getId(), source.getSnomed(), source.getStatusId(), source.getStatus(), source.getPatientId());
        this.verificationId = source.getVerificationId();
        this.verification = source.getVerification();
        this.problemId = source.getProblemId();
        this.startDate = source.getStartDate();
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
