package net.pladema.internation.repository.ips.generalstate;

import java.time.LocalDate;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.repository.masterdata.entity.ConditionVerificationStatus;
import net.pladema.internation.repository.masterdata.entity.ProblemType;
import net.pladema.internation.repository.masterdata.entity.Snomed;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HealthConditionVo extends ClinicalTermVo {

    private String verificationId;

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
		return Objects.hash(verificationId, problemId, noteId, note);
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
		return Objects.equals(verificationId, other.getVerificationId()) && 
			   Objects.equals(problemId, other.getProblemId()) &&
			   Objects.equals(noteId, other.getNoteId()) &&
			   Objects.equals(note, other.getNote());
	}

    public boolean isSecondaryDiagnosis() {
        return problemId.equals(ProblemType.DIAGNOSTICO) && !main;
    }
}
