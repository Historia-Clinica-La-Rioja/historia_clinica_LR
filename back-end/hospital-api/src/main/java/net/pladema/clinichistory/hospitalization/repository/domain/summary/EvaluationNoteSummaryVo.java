package net.pladema.clinichistory.hospitalization.repository.domain.summary;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class EvaluationNoteSummaryVo extends DocumentSummaryVo{

    public EvaluationNoteSummaryVo(Long id, String statusId) {
        super(id, statusId);
    }
}
