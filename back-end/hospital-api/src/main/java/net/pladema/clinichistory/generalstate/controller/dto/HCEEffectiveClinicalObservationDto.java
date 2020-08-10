package net.pladema.clinichistory.generalstate.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.sgx.dates.configuration.JacksonDateFormatConfig;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class HCEEffectiveClinicalObservationDto extends HCEClinicalObservationDto {

    @NotNull(message = "{clinical.observation.effective.time.mandatory}")
    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_TIME_FORMAT)
    private String effectiveTime;

}
