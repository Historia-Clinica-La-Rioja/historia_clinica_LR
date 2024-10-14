package net.pladema.establishment.sanitaryresponsibilityarea.institutionresponsibilityarea.infrastructure.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.infrastructure.input.dto.GlobalCoordinatesDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SaveInstitutionResponsibilityAreaDto {

	private List<GlobalCoordinatesDto> responsibilityAreaPolygon;

}
