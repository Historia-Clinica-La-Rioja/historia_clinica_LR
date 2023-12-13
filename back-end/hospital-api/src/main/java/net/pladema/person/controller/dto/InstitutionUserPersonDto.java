package net.pladema.person.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InstitutionUserPersonDto {

	private Integer id;

	private Integer personId;

	private Integer institutionId;

	private String completeName;

	private String completeLastName;

	private String identificationNumber;

}
