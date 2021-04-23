package net.pladema.clinichistory.hospitalization.controller.generalstate.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.sgx.dates.configuration.JacksonDateFormatConfig;

@Getter
@Setter
@ToString
public class AllergyConditionDto extends HealthConditionDto {

    private Short categoryId;

    private Short criticalityId;

    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_FORMAT)
    private String date;
}
