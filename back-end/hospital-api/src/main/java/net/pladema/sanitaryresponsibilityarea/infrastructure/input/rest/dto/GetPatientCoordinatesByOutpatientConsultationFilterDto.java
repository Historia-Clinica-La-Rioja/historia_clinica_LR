package net.pladema.sanitaryresponsibilityarea.infrastructure.input.rest.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.infrastructure.input.dto.GlobalCoordinatesDto;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class GetPatientCoordinatesByOutpatientConsultationFilterDto {

	@NotNull
	private GlobalCoordinatesDto mapUpperCorner;

	@NotNull
	private GlobalCoordinatesDto mapLowerCorner;

	@NotNull
	private DateDto fromDate;

	@NotNull
	private DateDto toDate;

}
