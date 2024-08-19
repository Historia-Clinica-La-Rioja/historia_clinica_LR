package net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain;

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
public class NominatimAddressBo {

	private String streetName;

	private String houseNumber;

	private String cityName;

	private String stateName;

	private String countryName;

	private String postalCode;

}
