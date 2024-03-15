package net.pladema.patient.service.domain;

import ar.lamansys.sgh.clinichistory.domain.ips.enums.EGender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PatientGenderAgeBo {

	private Integer id;
	private EGender gender;
	private LocalDate birthDate;

	public PatientGenderAgeBo(Integer id, Short genderId, LocalDate birthDate){
		this.id = id;
		this.gender = genderId != null ? EGender.map(genderId) : null;
		this.birthDate = birthDate;
	}

}
