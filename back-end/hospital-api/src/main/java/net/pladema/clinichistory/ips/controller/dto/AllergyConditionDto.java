package net.pladema.clinichistory.ips.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.sgx.dates.configuration.JacksonDateFormatConfig;

@Getter
@Setter
@ToString
public class AllergyConditionDto extends HealthConditionDto {

    private String categoryId;

    private String severity;

    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_FORMAT)
    private String date;
}
