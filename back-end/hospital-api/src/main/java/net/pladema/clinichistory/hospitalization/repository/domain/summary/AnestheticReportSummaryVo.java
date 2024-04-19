package net.pladema.clinichistory.hospitalization.repository.domain.summary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AnestheticReportSummaryVo extends DocumentSummaryVo {

    public AnestheticReportSummaryVo(Long id, String statusId) {
        super(id, statusId);
    }
}
