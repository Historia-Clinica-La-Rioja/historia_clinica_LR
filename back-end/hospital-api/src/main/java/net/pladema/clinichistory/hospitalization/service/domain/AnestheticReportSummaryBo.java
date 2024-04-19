package net.pladema.clinichistory.hospitalization.service.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.repository.domain.summary.AnestheticReportSummaryVo;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AnestheticReportSummaryBo extends DocumentSummaryBo {

    public AnestheticReportSummaryBo(AnestheticReportSummaryVo anestheticReportSummaryVo) {
        super(anestheticReportSummaryVo.getId(), anestheticReportSummaryVo.getStatusId());
    }
}
