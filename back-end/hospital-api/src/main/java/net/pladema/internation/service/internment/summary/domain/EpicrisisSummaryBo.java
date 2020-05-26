package net.pladema.internation.service.internment.summary.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.repository.internment.domain.summary.EpicrisisSummaryVo;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class EpicrisisSummaryBo extends DocumentSummaryBo {

    public EpicrisisSummaryBo(EpicrisisSummaryVo epicrisis) {
        super(epicrisis.getId(), epicrisis.getStatusId());
    }
}
