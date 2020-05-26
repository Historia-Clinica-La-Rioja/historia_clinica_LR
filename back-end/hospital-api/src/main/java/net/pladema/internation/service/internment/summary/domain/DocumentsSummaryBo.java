package net.pladema.internation.service.internment.summary.domain;


import lombok.*;
import net.pladema.internation.repository.internment.domain.summary.DocumentsSummaryVo;

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
