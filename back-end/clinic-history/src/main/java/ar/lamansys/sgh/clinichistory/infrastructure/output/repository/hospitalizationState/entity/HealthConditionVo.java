package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity;

import java.time.LocalDate;
import java.util.Objects;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.Snomed;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionVerificationStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ProblemType;
import ar.lamansys.sgh.shared.infrastructure.input.service.ProblemTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HealthConditionVo extends ClinicalTermVo {

    private String verificationId;

    private String verification;

    private String problemId;

    private LocalDate startDate;

    private Long noteId;

    private String note;

    private boolean main;

    private Short errorReasonId;

    private LocalDate endDate;

    private Short specificType;

    public HealthConditionVo(Integer id, Snomed snomed, String statusId, boolean main, String verificationId,
                             String problemId, LocalDate startDate, Long noteId, String note) {
        super(id, snomed, statusId);
        this.main = main;
        this.verificationId = verificationId;
        this.problemId = problemId;
        this.startDate = startDate;
        this.noteId = noteId;
        this.note = note;
    }

    public HealthConditionVo(Integer id, Snomed snomed, String statusId, String status, boolean main,  String verificationId,
                             String verification, String problemId, LocalDate startDate, LocalDate endDate, Long noteId,
                             String note, Short errorReasonId, Short specificType) {
        this(id, snomed, statusId, main,  verificationId,
                problemId, startDate, noteId, note);
        this.verification = verification;
        this.setStatus(status);
        this.errorReasonId = errorReasonId;
        this.specificType = specificType;
        this.endDate = endDate;
    }

    public boolean isDiagnosis() {
        return problemId.equals(ProblemType.DIAGNOSIS);
    }

    public boolean isPersonalHistory() {
        return ProblemType.PERSONAL_HISTORY.equals(problemId);
    }

    public boolean isFamilyHistory() {
        return problemId.equals(ProblemType.FAMILY_HISTORY);
    }

    public boolean isOtherHistory() {
        return ProblemType.OTHER_HISTORY.equals(problemId);
    }
	public boolean isOfType(ProblemTypeEnum type) {
		return problemId.equals(type.getId());
	}

	public boolean isProblem() {
		return isOfType(ProblemTypeEnum.PROBLEM) || isOfType(ProblemTypeEnum.CHRONIC);
	}

    public boolean isPresumptive() {
        return (verificationId != null && verificationId.equalsIgnoreCase(ConditionVerificationStatus.PRESUMPTIVE));
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
		HealthConditionVo other = (HealthConditionVo) obj;
		return Objects.equals(getId(), other.getId());
	}

    public boolean isSecondaryDiagnosis() {
        return problemId.equals(ProblemType.DIAGNOSIS) && !main;
    }
}
