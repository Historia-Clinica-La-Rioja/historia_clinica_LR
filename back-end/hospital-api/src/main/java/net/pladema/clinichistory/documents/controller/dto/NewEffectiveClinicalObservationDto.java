package net.pladema.clinichistory.documents.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;

@Getter
@Setter
@ToString
public class NewEffectiveClinicalObservationDto extends ClinicalObservationDto {

    private DateTimeDto effectiveTime;

}
