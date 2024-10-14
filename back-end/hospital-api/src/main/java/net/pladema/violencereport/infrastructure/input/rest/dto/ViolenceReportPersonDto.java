package net.pladema.violencereport.infrastructure.input.rest.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class ViolenceReportPersonDto {

	private String lastName;

	private String firstName;

	private Short age;

	private ViolenceReportAddressDto address;

}
