package net.pladema.clinichistory.hospitalization.service.domain;


import lombok.*;
import net.pladema.clinichistory.hospitalization.repository.domain.summary.DocumentsSummaryVo;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DocumentsSummaryBo {

    private AnamnesisSummaryBo anamnesis;

    private EpicrisisSummaryBo epicrisis;

    private EvaluationNoteSummaryBo lastEvaluationNote;

    public DocumentsSummaryBo(DocumentsSummaryVo documents) {
        this.anamnesis = new AnamnesisSummaryBo(documents.getAnamnesis());
        this.epicrisis = new EpicrisisSummaryBo(documents.getEpicrisis());
        this.lastEvaluationNote = new EvaluationNoteSummaryBo(documents.getLastEvaluationNote());
    }
}
