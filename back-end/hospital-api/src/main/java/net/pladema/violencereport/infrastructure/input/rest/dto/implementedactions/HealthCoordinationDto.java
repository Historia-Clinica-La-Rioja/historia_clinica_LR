package net.pladema.violencereport.infrastructure.input.rest.dto.implementedactions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class HealthCoordinationDto {

	private CoordinationInsideHealthSectorDto coordinationInsideHealthSector;

	private CoordinationOutsideHealthSectorDto coordinationOutsideHealthSector;

}
