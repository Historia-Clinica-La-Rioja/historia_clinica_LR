package net.pladema.clinichistory.hospitalization.controller.generalstate.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.sgx.dates.configuration.JacksonDateFormatConfig;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@ToString
@Validated
public class InmunizationDto extends ClinicalTermDto {

    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_FORMAT)
    private String administrationDate;

    private String note;
}
