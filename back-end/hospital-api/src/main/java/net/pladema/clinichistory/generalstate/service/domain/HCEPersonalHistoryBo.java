package net.pladema.clinichistory.generalstate.service.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.generalstate.repository.domain.HCEHealthConditionVo;
import net.pladema.clinichistory.ips.repository.masterdata.entity.ConditionClinicalStatus;
import net.pladema.clinichistory.ips.repository.masterdata.entity.ProblemType;
import net.pladema.sgx.dates.configuration.LocalDateMapper;

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
        return problemId.equals(ProblemType.CHRONIC);
    }

    public boolean isActiveProblem() {
        return getStatusId().equals(ConditionClinicalStatus.ACTIVE);
    }

    public boolean isSolvedProblem() {
        return getStatusId().equals(ConditionClinicalStatus.SOLVED);
    }
}
