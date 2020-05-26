package net.pladema.internation.service.internment.summary.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.repository.internment.domain.summary.EvaluationNoteSummaryVo;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class EvaluationNoteSummaryBo extends DocumentSummaryBo {

    public EvaluationNoteSummaryBo(EvaluationNoteSummaryVo lastEvaluationNote) {
        super(lastEvaluationNote.getId(), lastEvaluationNote.getStatusId());
    }
}
