package net.pladema.staff.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BackofficeHealthcareProfessionalCompleteDto {

	private Integer id;

	private Integer personId;

	private String firstName;

	private String lastName;

	private Short identificationTypeId;

	private String identificationNumber;

}
