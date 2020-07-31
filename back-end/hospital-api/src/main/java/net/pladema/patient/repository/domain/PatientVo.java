package net.pladema.patient.repository.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.person.repository.domain.BasicPersonVo;
import net.pladema.person.repository.entity.Person;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PatientVo implements Serializable {

	private static final long serialVersionUID = 7559172653664261066L;

	private Integer id;

	private BasicPersonVo person;

	public PatientVo(Integer patientId, Person person, String identificationType) {
		this.person = new BasicPersonVo(person.getFirstName(), person.getLastName(), identificationType,
				person.getIdentificationNumber());
		this.id = patientId;
	}

}
