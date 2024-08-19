package net.pladema.clinichistory.hospitalization.repository.domain.summary;


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
public class DocumentsSummaryVo {

    private AnamnesisSummaryVo anamnesis;

    private EpicrisisSummaryVo epicrisis;

    private EvaluationNoteSummaryVo lastEvaluationNote;

    private AnestheticReportSummaryVo lastAnestheticReport;
}
