package net.pladema.staff.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.person.repository.entity.Person;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class HealthcarePersonBo {

	private Integer id;
	private String licenceNumber;
	private Integer personId;
	private Person person;
	private String nameSelfDetermination;
	
}
