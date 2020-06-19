package net.pladema.clinichistory.hospitalization.service.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.repository.domain.summary.AnamnesisSummaryVo;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AnamnesisSummaryBo extends DocumentSummaryBo {

    public AnamnesisSummaryBo(AnamnesisSummaryVo anamnesis) {
        super(anamnesis.getId(), anamnesis.getStatusId());
    }
}
