package net.pladema.person.repository;

import ar.lamansys.sgx.shared.repositories.QueryPart;
import net.pladema.person.repository.domain.DuplicatePersonVo;
import net.pladema.person.repository.domain.PersonSearchResultVo;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PersonSearchQuery {

	String firstName;
	String middleNames;
	String lastName;
	String otherLastNames;
	Short identificationTypeId;
	String identificationNumber;
	LocalDate birthdate;

	public PersonSearchQuery(DuplicatePersonVo duplicatePersonVo) {
		this.firstName = duplicatePersonVo.getFirstName();
		this.middleNames = duplicatePersonVo.getMiddleNames();
		this.lastName = duplicatePersonVo.getLastName();
		this.otherLastNames = duplicatePersonVo.getOtherLastNames();
		this.identificationTypeId = duplicatePersonVo.getIdentificationTypeId();
		this.identificationNumber = duplicatePersonVo.getIdentificationNumber();
		this.birthdate = duplicatePersonVo.getBirthdate();
	}

	public QueryPart select() {
		String select = " patient.id, \n" +
				" person.first_name, \n" +
				" person.middle_names, \n" +
				" person.last_name, \n" +
				" person.other_last_names, \n" +
				" person.identification_type_id, \n" +
				" person.identification_number, \n" +
				" person.birth_date, \n" +
				" person.gender_id, \n" +
				" person_extended.phone_prefix, \n" +
				" person_extended.phone_number, \n" +
				" person_extended.name_self_determination, \n" +
				" patient.type_id \n";
		return new QueryPart(select);
	}

	public QueryPart from() {
		String from = " {h-schema}person as person \n" +
				"	join {h-schema}patient as patient on (patient.person_id = person.id) and (patient.deleted != true) \n" +
				"	join {h-schema}person_extended as person_extended on (person_extended.person_id = person.id) \n";
		return new QueryPart(from);
	}

	public QueryPart where() {
		String where = "";
		if (firstName != null && !firstName.isEmpty())
			where += " LOWER(person.first_name) = '" + firstName.toLowerCase() + "' AND \n";
		if (middleNames != null && !middleNames.isEmpty())
			where += " LOWER(person.middle_names) = '" + middleNames.toLowerCase() + "' AND \n";
		if (lastName != null && !lastName.isEmpty())
			where += " LOWER(person.last_name) = '" + lastName.toLowerCase() + "' AND \n";
		if (otherLastNames != null && !otherLastNames.isEmpty())
			where += " LOWER(person.other_last_names) = '" + otherLastNames.toLowerCase() + "' AND \n";
		if (identificationTypeId != null)
			where += " person.identification_type_id = " + identificationTypeId + " AND \n";
		if (identificationNumber != null && !identificationNumber.isEmpty())
			where += " person.identification_number = '" + identificationNumber + "' AND \n";
		if (birthdate != null)
			where += " person.birth_date = '" + birthdate.toString() + "' AND \n";
		where = where.substring(0,where.length()-6);
		where += " \n";
		return new QueryPart(where);
	}

	public List<PersonSearchResultVo> construct(List<Object[]> resultQuery) {
		List<PersonSearchResultVo> result = new ArrayList<>();
		for (Object[] objects : resultQuery) {
			Integer patientId = null;
			String firstName = null;
			String middleNames = null;
			String lastName = null;
			String otherLastNames = null;
			Short identifyTypeId = null;
			String identifyNumber = null;
			Date birthdate = null;
			LocalDate birthdateLD = null;
			Short genderId = null;
			String phonePrefix = null;
			String phoneNumber = null;
			String nameSelfDetermination = null;
			Short typeId = null;
			patientId = (Integer) objects[0];
			firstName = (String) objects[1];
			middleNames = (String) objects[2];
			lastName = (String) objects[3];
			otherLastNames = (String) objects[4];
			identifyTypeId = (Short) objects[5];
			identifyNumber = (String) objects[6];
			birthdate = (Date) objects[7];
			genderId = (Short) objects[8];
			phonePrefix = (String) objects[9];
			phoneNumber = (String) objects[10];
			nameSelfDetermination = (String) objects[11];
			typeId = (Short) objects[12];
			if (birthdate != null)
				birthdateLD = birthdate.toLocalDate();
			result.add(new PersonSearchResultVo(patientId,firstName,middleNames,lastName,otherLastNames,identifyTypeId,identifyNumber,birthdateLD,genderId,phonePrefix,phoneNumber,nameSelfDetermination,typeId));
		}
		return result;
	}

}
