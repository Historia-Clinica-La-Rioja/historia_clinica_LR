package ar.lamansys.sgh.shared.infrastructure.input.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Builder
@ToString
@Getter
public class PersonInfoDto {

	private String firstName;

	private String middleNames;

	private String lastName;

	private String otherLastNames;

	private String identificationNumber;

	private String identificationTypeDescription;

	private LocalDate birthDate;

}
