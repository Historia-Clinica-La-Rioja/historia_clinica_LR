package net.pladema.emergencycare.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.person.repository.entity.Person;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonECEVo implements Serializable {

	private static final long serialVersionUID = 6315285696051865362L;

	private Integer id;

	private String firstName;

	private String lastName;

	private String identificationNumber;

	private String photo;

	private String nameSelfDetermination;

	public PersonECEVo(Person person){
		if (person !=null) {
			this.id = person.getId();
			this.firstName = person.getFirstName();
			this.lastName = person.getLastName();
			this.identificationNumber = person.getIdentificationNumber();
		}
	}

	public PersonECEVo(Person person, String nameSelfDetermination){
		if (person !=null) {
			this.id = person.getId();
			this.firstName = person.getFirstName();
			this.lastName = person.getLastName();
			this.identificationNumber = person.getIdentificationNumber();
			this.nameSelfDetermination = nameSelfDetermination;
		}
	}

}
