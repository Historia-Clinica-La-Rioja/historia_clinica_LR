package net.pladema.clinichistory.hospitalization.repository.domain.summary;


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
