package net.pladema.person.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.address.repository.entity.Address;
import net.pladema.address.repository.entity.City;
import net.pladema.address.repository.entity.Country;
import net.pladema.address.repository.entity.Department;
import net.pladema.address.repository.entity.Province;
import net.pladema.person.repository.entity.Person;
import net.pladema.person.repository.entity.PersonExtended;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompletePersonVo {

	private Person person;

	private PersonExtended personExtended;
	
	private Address address;
	
	private City city;

	private Province province;
	
	private Department department;

	private Country country;

}
