package ar.lamansys.sgh.clinichistory.infrastructure.input.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PatientInfoDto {

	private Integer id;

	private Short genderId;

	private Short age;

}