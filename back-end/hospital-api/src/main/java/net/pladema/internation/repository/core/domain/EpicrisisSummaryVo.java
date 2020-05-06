package net.pladema.internation.repository.core.domain;

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
