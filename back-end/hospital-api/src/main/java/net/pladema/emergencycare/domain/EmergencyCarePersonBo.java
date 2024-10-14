package net.pladema.emergencycare.domain;

import ar.lamansys.sgh.clinichistory.domain.ips.enums.EGender;
import lombok.Getter;
import lombok.Setter;
import net.pladema.person.repository.domain.PersonAgeBo;
import net.pladema.person.repository.entity.Person;

@Setter
@Getter
public class EmergencyCarePersonBo {

	private Integer id;
	private String firstName;
	private String middleNames;
	private String lastName;
	private String otherLastNames;
	private String identificationNumber;
	private String nameSelfDetermination;
	private String identificationType;
	private String gender;
	private PersonAgeBo age;

	public EmergencyCarePersonBo(Person p, String nameSelfDetermination, String identificationTypeDescription){
		this.id = p.getId();
		this.firstName = p.getFirstName();
		this.middleNames = p.getMiddleNames();
		this.lastName = p.getLastName();
		this.otherLastNames = p.getOtherLastNames();
		this.identificationNumber = p.getIdentificationNumber();
		this.nameSelfDetermination = nameSelfDetermination;
		this.identificationType = identificationTypeDescription;
		this.gender = EGender.map(p.getGenderId()).getValue();
		this.age = new PersonAgeBo(p.getBirthDate());
	}
}
