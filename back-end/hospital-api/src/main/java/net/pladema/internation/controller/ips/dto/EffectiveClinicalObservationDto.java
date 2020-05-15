package net.pladema.internation.controller.ips.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.dates.configuration.JacksonDateFormatConfig;
import net.pladema.internation.controller.ips.constraints.BeforeOrPresent;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class EffectiveClinicalObservationDto extends ClinicalObservationDto {

    @NotNull
    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_TIME_FORMAT)
    @BeforeOrPresent
    private String effectiveTime;

}
