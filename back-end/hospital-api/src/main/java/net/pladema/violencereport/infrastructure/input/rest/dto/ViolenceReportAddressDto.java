package net.pladema.violencereport.infrastructure.input.rest.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.address.controller.dto.CityDto;
import net.pladema.address.controller.dto.DepartmentDto;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ViolenceReportAddressDto {

	private String homeAddress;

	private CityDto city;

	private DepartmentDto municipality;

}
