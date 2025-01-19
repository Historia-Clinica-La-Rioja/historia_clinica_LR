package net.pladema.clinichistory.hospitalization.service.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AnestheticReportSummaryBo extends DocumentSummaryBo {

    public AnestheticReportSummaryBo(Long id, String statusId) {
        super(id, statusId);
    }
}
