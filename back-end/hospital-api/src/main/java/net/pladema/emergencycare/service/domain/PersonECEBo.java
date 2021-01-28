package net.pladema.emergencycare.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.emergencycare.repository.domain.PersonECEVo;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PersonECEBo {

	private Integer id;

	private String firstName;

	private String lastName;

	private String photo;

	public PersonECEBo(PersonECEVo person){
		this.id = person.getId();
		this.firstName = person.getFirstName();
		this.lastName = person.getLastName();
		this.photo = person.getPhoto();
	}
}
