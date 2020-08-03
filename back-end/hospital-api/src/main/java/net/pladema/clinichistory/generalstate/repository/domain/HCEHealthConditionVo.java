package net.pladema.clinichistory.generalstate.repository.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.ips.repository.masterdata.entity.ConditionVerificationStatus;
import net.pladema.clinichistory.ips.repository.masterdata.entity.ProblemType;
import net.pladema.clinichistory.ips.repository.masterdata.entity.Snomed;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class HCEHealthConditionVo extends HCEClinicalTermVo {

    private String verificationId;

    private String verification;

    private String problemId;

    private LocalDate startDate;

    private LocalDate inactivationDate;

    private boolean main;

    private Integer patientId;

    public HCEHealthConditionVo(Integer id, Snomed snomed, String statusId, boolean main, String verificationId,
                                String problemId, LocalDate startDate, LocalDate inactivationDate, Integer patientId) {
        super(id, snomed, statusId);
        this.main = main;
        this.verificationId = verificationId;
        this.problemId = problemId;
        this.startDate = startDate;
        this.inactivationDate = inactivationDate;
        this.patientId = patientId;
    }

    public boolean isDiagnosis() {
        return problemId.equals(ProblemType.DIAGNOSTICO);
    }

    public boolean isPersonalHistory() {
        return problemId.equals(ProblemType.PROBLEMA);
    }

    public boolean isFamilyHistory() {
        return problemId.equals(ProblemType.ANTECEDENTE);
    }

    public boolean isChronic() {
        return problemId.equals(ProblemType.CHRONIC);
    }

    public boolean isPresumptive() {
        return (verificationId != null && verificationId.equalsIgnoreCase(ConditionVerificationStatus.PRESUMPTIVE));
    }

    public boolean isSecondaryDiagnosis() {
        return problemId.equals(ProblemType.DIAGNOSTICO) && !main;
    }

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HCEHealthConditionVo other = (HCEHealthConditionVo) obj;
		return Objects.equals(getId(), other.getId());
	}
}
