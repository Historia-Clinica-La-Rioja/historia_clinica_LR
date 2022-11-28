package net.pladema.patient.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.person.repository.entity.Person;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class PatientSearch {

	private Person person;
	private Integer idPatient;
	private Boolean activo;
	private float ranking;
	private String nameSelfDetermination;
	
	public PatientSearch(Person person, int idPatient, boolean activo, int ranking, String nameSelfDetermination) {
		this.person = person;
		this.idPatient = idPatient;
		this.activo = activo;
		this.ranking = ranking;
		this.nameSelfDetermination = nameSelfDetermination;
	}
	
}
