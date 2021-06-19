package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class EffectiveClinicalObservationDto extends ClinicalObservationDto {

    @NotNull(message = "{clinical.observation.effective.time.mandatory}")
    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_TIME_FORMAT)
    private String effectiveTime;

}
