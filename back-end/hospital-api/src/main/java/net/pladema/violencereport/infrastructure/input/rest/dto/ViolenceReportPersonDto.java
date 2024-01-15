package net.pladema.violencereport.infrastructure.input.rest.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.address.controller.dto.DepartmentDto;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class ViolenceReportPersonDto {

	private String lastName;

	private String firstName;

	private Short age;

	private String address;

	private DepartmentDto municipality;

}
