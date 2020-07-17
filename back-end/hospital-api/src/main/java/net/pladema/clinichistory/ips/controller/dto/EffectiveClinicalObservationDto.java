package net.pladema.clinichistory.ips.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.sgx.dates.configuration.JacksonDateFormatConfig;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class EffectiveClinicalObservationDto extends ClinicalObservationDto {

    @NotNull(message = "{clinicalobservation.effetivetime.mandatory}")
    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_TIME_FORMAT)
    private String effectiveTime;

}
