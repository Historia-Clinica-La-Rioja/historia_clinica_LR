package net.pladema.medicalconsultation.virtualConsultation.infrastructure.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VirtualConsultationPatientDataDto {

	private Integer id;

	private String name;

	private String lastName;

	private Integer age;

	private String gender;

}
