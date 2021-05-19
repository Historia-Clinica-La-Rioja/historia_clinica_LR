package net.pladema.clinichistory.outpatient.createoutpatient.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.ClinicalTermDto;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OutpatientSummaryHealthConditionDto extends ClinicalTermDto {

    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_FORMAT)
    private String startDate;

    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_FORMAT)
    private String inactivationDate;

    private boolean main;

    private String problemId;
}
