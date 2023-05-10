package net.pladema.patient.repository;


import ar.lamansys.sgx.shared.repositories.QueryPart;
import ar.lamansys.sgx.shared.repositories.QueryStringHelper;
import net.pladema.patient.controller.dto.PatientRegistrationSearchFilter;
import net.pladema.patient.service.domain.PatientRegistrationSearch;
import net.pladema.person.repository.entity.Person;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class PatientRegistrationSearchQuery {

	private static final String LIKE_FORMAT = " (UPPER(%s) LIKE '%%%s%%') \n"; //double '%' to escape the character
	private static final String LIKE_FORMAT_INDEX = " (UPPER(%s) LIKE '%s%%') \n"; //double '%' to escape the character
	private static final String EQUAL_FORMAT = " (UPPER(%s) = '%s') \n";

	String firstName;
	String middleNames;
	String lastName;
	String otherLastNames;
	Short genderId;
	Short identificationTypeId;
	String identificationNumber;
	LocalDate birthDate;
	Boolean toAudit;
	Boolean automaticValidation;
	Boolean manualValidation;
	Boolean temporary;
	Boolean permanentNotValidated;
	Boolean validated;
	Boolean permanent;

	public PatientRegistrationSearchQuery(PatientRegistrationSearchFilter patientRegistrationSearchFilter) {
		this.firstName = patientRegistrationSearchFilter.getFirstName();
		this.middleNames = patientRegistrationSearchFilter.getMiddleNames();
		this.lastName = patientRegistrationSearchFilter.getLastName();
		this.otherLastNames = patientRegistrationSearchFilter.getOtherLastNames();
		this.genderId = patientRegistrationSearchFilter.getGenderId();
		this.identificationTypeId = patientRegistrationSearchFilter.getIdentificationTypeId();
		this.identificationNumber = patientRegistrationSearchFilter.getIdentificationNumber();
		this.birthDate = patientRegistrationSearchFilter.getBirthDate();
		this.toAudit = patientRegistrationSearchFilter.getToAudit();
		this.automaticValidation = patientRegistrationSearchFilter.getAutomaticValidation();
		this.manualValidation = patientRegistrationSearchFilter.getManualValidation();
		this.temporary = patientRegistrationSearchFilter.getTemporary();
		this.permanentNotValidated = patientRegistrationSearchFilter.getPermanentNotValidated();
		this.validated = patientRegistrationSearchFilter.getValidated();
		this.permanent = patientRegistrationSearchFilter.getPermanent();
	}

	public QueryPart select() {
		String select = " patient.id as patientId, \n" +
				" person.id, \n" +
				" person.first_name, \n" +
				" person.middle_names, \n" +
				" person.last_name, \n" +
				" person.other_last_names, \n" +
				" person.gender_id, \n" +
				" person.identification_type_id, \n" +
				" person.identification_number, \n" +
				" person.birth_date, \n" +
				" patient.type_id as patientTypeId, \n" +
				" patient.to_audit as patientToAudit , \n" +
				" personExtended.name_self_determination \n";
		return new QueryPart(select);
	}

	public QueryPart from() {
		String from = "	{h-schema}patient as patient \n" +
				"	join {h-schema}person as person on (patient.person_id = person.id) \n" +
				" 	left join {h-schema}person_extended as personExtended on ( personExtended.person_id = person.id) \n";
		return new QueryPart(from);
	}

	public QueryPart whereWithBasicAttributes() {
		List<String> whereString = new ArrayList<>();

		if (firstName != null) {
			firstName = (QueryStringHelper.escapeSql(firstName)).toUpperCase();
			String clause = String.format(LIKE_FORMAT_INDEX, "person.first_name", firstName);
			whereString.add(clause);
		}
		if (lastName != null) {
			lastName = (QueryStringHelper.escapeSql(lastName)).toUpperCase();
			String clause = String.format(LIKE_FORMAT_INDEX, "person.last_name", lastName);
			whereString.add(clause);
		}
		if (identificationNumber != null){
			String clause = " (person.identification_number = '" + identificationNumber+"')";
			whereString.add(clause);
		}
		if (birthDate != null) {
			String birthDateString = (QueryStringHelper.escapeSql(birthDate.toString())).toUpperCase();
			whereString.add(" (person.birth_date = '"+birthDateString+"') \n");
		}

		QueryPart result = new QueryPart(" AND ( ")
				.concatPart(new QueryPart(String.join(" OR ", whereString)))
				.concat(" )");
		return result;
	}

	public QueryPart whereWithPatientTypesValidation() {
		List<String> whereString = new ArrayList<>();

		if (validated != null && validated) {
			String clause = " patient.type_id = 2 ";
			whereString.add(clause);
		}
		if (permanentNotValidated != null && permanentNotValidated) {
			String clause = " patient.type_id = 7 ";
			whereString.add(clause);
		}
		if (temporary != null && temporary) {
			String clause = " patient.type_id = 3 ";
			whereString.add(clause);
		}
		if (permanent != null && permanent) {
			String clause = " patient.type_id = 1 ";
			whereString.add(clause);
		}

		QueryPart result = new QueryPart(" AND ( ")
				.concatPart(new QueryPart(String.join(" OR ", whereString)))
				.concat(" )");

		return result;

	}

	public QueryPart whereWithToAuditValidation() {
		String clause = "";

		if (toAudit != null && toAudit) {
			clause = " AND ( patient.to_audit = true ) ";
		}

		return new QueryPart(clause);
	}

	public List<PatientRegistrationSearch> construct(List<Object[]> resultQuery){
		List<PatientRegistrationSearch> result = new ArrayList<>();

		Map<Integer, List<Object[]>> diagnosisByDocuments = resultQuery
				.stream()
				.collect(Collectors.groupingBy(
						(Object[] t) -> (Integer)t[0],
						LinkedHashMap::new,
						toList())
				);
		diagnosisByDocuments.forEach((id,v) -> {
			Object[] tuple = v.get(0);

			result.add(new PatientRegistrationSearch(mapPerson(tuple), id, (Short) tuple[10], (Boolean) tuple[11], (String) tuple[12]));

		});
		return result;
	}

	private Person mapPerson(Object[] tuple) {
		Integer id = (Integer) tuple[1];
		String firstNamePerson = (String) tuple[2];
		String middleNamesPerson = (String) tuple[3];
		String lastNamePerson = (String) tuple[4];
		String otherLastNamesPerson = (String) tuple[5];
		Short genderIdPerson = (Short) tuple[6];
		Short identificationTypeIdPerson = (Short) tuple[7];
		String identificationNumberPerson = (String) tuple[8];
		LocalDate birthDatePerson = tuple[9] != null ? ((Date) tuple[9]).toLocalDate() : null;
		return new Person(id, firstNamePerson, middleNamesPerson, lastNamePerson, otherLastNamesPerson, identificationTypeIdPerson, identificationNumberPerson, genderIdPerson, birthDatePerson);
	}

}
