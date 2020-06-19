package net.pladema.clinichistory.ips.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
public class ReportClinicalObservationDto extends ClinicalObservationDto {

    private ZonedDateTime effectiveTime;

}
