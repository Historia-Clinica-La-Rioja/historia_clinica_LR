package net.pladema.clinichistory.hospitalization.controller.generalstate.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.ips.controller.dto.ClinicalObservationDto;

import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
public class ReportClinicalObservationDto extends ClinicalObservationDto {

    private ZonedDateTime effectiveTime;

}
