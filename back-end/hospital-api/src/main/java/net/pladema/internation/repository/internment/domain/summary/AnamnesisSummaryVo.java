package net.pladema.internation.repository.internment.domain.summary;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AnamnesisSummaryVo extends DocumentSummaryVo {


    public AnamnesisSummaryVo(Long anamnesisDocId, String anamnesisStatusId) {
        super(anamnesisDocId, anamnesisStatusId);
    }
}
