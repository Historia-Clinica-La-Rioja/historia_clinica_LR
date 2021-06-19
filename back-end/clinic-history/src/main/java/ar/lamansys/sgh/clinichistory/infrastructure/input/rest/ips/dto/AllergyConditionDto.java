package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;

@Getter
@Setter
@ToString
public class AllergyConditionDto extends HealthConditionDto {

    private Short categoryId;

    private Short criticalityId;

    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_FORMAT)
    private String date;
}
