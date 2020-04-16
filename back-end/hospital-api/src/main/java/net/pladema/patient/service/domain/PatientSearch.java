package net.pladema.patient.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.pladema.person.repository.entity.Person;

@Getter
@Setter
@AllArgsConstructor
public class PatientSearch {

	private Person person;
	
	private float ranking;
	
}
