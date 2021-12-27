package net.pladema.clinichistory.outpatient.createoutpatient.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.ClinicalTermDto;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;

import java.util.List;

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

    private List<OutpatientSummaryReferenceDto> references;
}
