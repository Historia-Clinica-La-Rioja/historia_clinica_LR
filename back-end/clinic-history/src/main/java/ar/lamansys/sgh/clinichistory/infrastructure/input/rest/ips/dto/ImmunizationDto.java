package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@ToString
@Validated
public class ImmunizationDto extends ClinicalTermDto {

    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_FORMAT)
    private String administrationDate;

    private String note;
}
