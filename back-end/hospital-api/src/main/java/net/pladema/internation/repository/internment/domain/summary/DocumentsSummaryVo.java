package net.pladema.internation.repository.internment.domain.summary;


import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DocumentsSummaryVo {

    private AnamnesisSummaryVo anamnesis;

    private EpicrisisSummaryVo epicrisis;

    private EvaluationNoteSummaryVo lastEvaluationNote;
}
