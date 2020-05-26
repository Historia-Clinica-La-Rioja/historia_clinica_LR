package net.pladema.internation.service.internment.summary.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.repository.internment.domain.summary.AnamnesisSummaryVo;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AnamnesisSummaryBo extends DocumentSummaryBo {

    public AnamnesisSummaryBo(AnamnesisSummaryVo anamnesis) {
        super(anamnesis.getId(), anamnesis.getStatusId());
    }
}
