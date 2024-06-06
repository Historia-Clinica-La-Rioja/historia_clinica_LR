package net.pladema.nominatim.fetchglobalcoordinatesbyaddress.infrastructure.input.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GlobalCoordinatesDto {

	private Double latitude;

	private Double longitude;

}
