package ar.lamansys.sgh.shared.infrastructure.input.service;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Builder
public class SharedAddressDto {

	private String street;

	private String number;

	private String floor;

	private String apartment;

	private String postCode;

	private String cityName;

	private String departmentName;

	private String countryName;

	private String bahraCode;
	
}
