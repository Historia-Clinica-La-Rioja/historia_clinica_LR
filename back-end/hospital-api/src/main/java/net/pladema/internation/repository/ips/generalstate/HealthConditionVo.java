package net.pladema.internation.repository.ips.generalstate;

import lombok.*;
import net.pladema.internation.repository.masterdata.entity.ProblemType;
import net.pladema.internation.repository.masterdata.entity.Snomed;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HealthConditionVo {

    private Integer id;

    private Snomed snomed;

    private String statusId;

    private String verificationId;

    private String problemId;

    private LocalDate startDate;

    private String note;

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HealthConditionVo healthCondition = (HealthConditionVo) o;
        return Objects.equals(id, healthCondition.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, snomed);
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
}
