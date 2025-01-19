package net.pladema.sanitaryresponsibilityarea.infrastructure.input.rest.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.infrastructure.input.dto.GlobalCoordinatesDto;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class GetPatientCoordinatesByAddedInstitutionFilterDto {

	@NotNull
	private GlobalCoordinatesDto mapUpperCorner;

	@NotNull
	private GlobalCoordinatesDto mapLowerCorner;

}
