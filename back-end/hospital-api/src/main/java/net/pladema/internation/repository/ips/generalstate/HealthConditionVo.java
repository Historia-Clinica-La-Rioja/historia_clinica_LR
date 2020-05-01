package net.pladema.internation.repository.ips.generalstate;

import lombok.*;
import net.pladema.internation.repository.masterdata.entity.ConditionVerificationStatus;
import net.pladema.internation.repository.masterdata.entity.ProblemType;
import net.pladema.internation.repository.masterdata.entity.Snomed;

import java.time.LocalDate;

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

    public HealthConditionVo(Integer id, Snomed snomed, String statusId, String verificationId,
                             String problemId, LocalDate startDate, Long noteId, String note) {
        super(id, snomed, statusId);
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
}
