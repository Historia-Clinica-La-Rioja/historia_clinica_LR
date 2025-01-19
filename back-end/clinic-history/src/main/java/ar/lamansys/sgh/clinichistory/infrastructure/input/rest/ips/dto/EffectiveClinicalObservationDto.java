package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;

import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
public class EffectiveClinicalObservationDto extends ClinicalObservationDto {

    @Nullable
    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_TIME_FORMAT)
    private String effectiveTime;

    @JsonIgnore
    public LocalDateTime getLocalDateEffectiveTime() {
        if (effectiveTime == null) {
            return LocalDateTime.now();
        }
        return LocalDateTime.parse(effectiveTime, DateTimeFormatter.ofPattern(JacksonDateFormatConfig.DATE_TIME_FORMAT));
    }
}
