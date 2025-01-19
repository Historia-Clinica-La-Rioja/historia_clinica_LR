package net.pladema.clinichistory.hospitalization.controller.dto.summary;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class DocumentsSummaryDto implements Serializable {

    private static final long serialVersionUID = -7491459331107772563L;

    private AnamnesisSummaryDto anamnesis;

    private EpicrisisSummaryDto epicrisis;

    private EvaluationNoteSummaryDto lastEvaluationNote;

    private AnestheticReportSummaryDto lastAnestheticReport;

}
