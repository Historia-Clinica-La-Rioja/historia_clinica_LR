package net.pladema.clinichistory.hospitalization.service.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.repository.domain.summary.EvaluationNoteSummaryVo;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class EvaluationNoteSummaryBo extends DocumentSummaryBo {

    public EvaluationNoteSummaryBo(EvaluationNoteSummaryVo lastEvaluationNote) {
        super(lastEvaluationNote.getId(), lastEvaluationNote.getStatusId());
    }
}
