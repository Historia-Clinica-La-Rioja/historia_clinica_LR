package net.pladema.internation.repository.internment.domain.summary;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class EpicrisisSummaryVo extends DocumentSummaryVo{

    public EpicrisisSummaryVo(Long epicrisisDocId, String epicrisisStatusId) {
        super(epicrisisDocId, epicrisisStatusId);
    }
}
