package ar.lamansys.sgh.shared.infrastructure.input.service.patient;

import ar.lamansys.sgh.shared.infrastructure.input.service.GenderDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.PersonAgeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PatientGenderAgeDto {

	private Integer id;
	private GenderDto gender;
	private PersonAgeDto age;

}
