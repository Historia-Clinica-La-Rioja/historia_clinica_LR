package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
public class ReportClinicalObservationDto extends ClinicalObservationDto {

    private ZonedDateTime effectiveTime;

    @JsonIgnore
    public LocalDateTime getLocalDateEffectiveTime() {
        if (effectiveTime == null) {
            return LocalDateTime.now();
        }
        return effectiveTime.toLocalDateTime();
    }
}
