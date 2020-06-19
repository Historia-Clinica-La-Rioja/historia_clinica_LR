package net.pladema.clinichistory.ips.repository.generalstate;

import lombok.*;
import net.pladema.clinichistory.ips.repository.masterdata.entity.ConditionVerificationStatus;
import net.pladema.clinichistory.ips.repository.masterdata.entity.ProblemType;
import net.pladema.clinichistory.ips.repository.masterdata.entity.Snomed;

import java.time.LocalDate;
import java.util.Objects;

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

    public HealthConditionVo(Integer id, Snomed snomed, String statusId, boolean main,  String verificationId,
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
                             String verification, String problemId, LocalDate startDate, Long noteId, String note) {
        this(id, snomed, statusId, main,  verificationId,
                problemId, startDate, noteId, note);
        this.verification = verification;
        this.setStatus(status);
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
        return problemId.equals(ProblemType.DIAGNOSTICO) && !main;
    }
}
