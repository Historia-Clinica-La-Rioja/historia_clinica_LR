package net.pladema.clinichistory.generalstate.service.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.generalstate.repository.domain.HCEHealthConditionVo;

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

    private boolean main;

    public HCEPersonalHistoryBo(HCEHealthConditionVo source){
        super(source.getId(), source.getSnomed(), source.getStatusId(), source.getStatus(), source.getPatientId());
        this.verificationId = source.getVerificationId();
        this.verification = source.getVerification();
        this.problemId = source.getProblemId();
        this.startDate = source.getStartDate();
        this.main = source.isMain();
    }
}
