package net.pladema.nominatim.fetchglobalcoordinatesbyaddress.infrastructure.input.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GlobalCoordinatesDto {

	private Double latitude;

	private Double longitude;

}
