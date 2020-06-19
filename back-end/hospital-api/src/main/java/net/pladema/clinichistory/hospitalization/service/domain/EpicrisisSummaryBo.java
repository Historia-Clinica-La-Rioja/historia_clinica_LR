package net.pladema.clinichistory.hospitalization.service.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.repository.domain.summary.EpicrisisSummaryVo;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class EpicrisisSummaryBo extends DocumentSummaryBo {

    public EpicrisisSummaryBo(EpicrisisSummaryVo epicrisis) {
        super(epicrisis.getId(), epicrisis.getStatusId());
    }
}
