package net.pladema.patient.repository;


import ar.lamansys.sgx.shared.repositories.QueryPart;
import ar.lamansys.sgx.shared.repositories.QueryStringHelper;
import lombok.NoArgsConstructor;
import net.pladema.patient.controller.dto.MergedPatientSearchFilter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class MergedPatientSearchQuery {

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
	Boolean temporary;
	Boolean permanentNotValidated;
	Boolean validated;
	Boolean permanent;

	public MergedPatientSearchQuery(MergedPatientSearchFilter mergedPatientSearchFilter) {
		this.firstName = mergedPatientSearchFilter.getFirstName();
		this.middleNames = mergedPatientSearchFilter.getMiddleNames();
		this.lastName = mergedPatientSearchFilter.getLastName();
		this.otherLastNames = mergedPatientSearchFilter.getOtherLastNames();
		this.genderId = mergedPatientSearchFilter.getGenderId();
		this.identificationTypeId = mergedPatientSearchFilter.getIdentificationTypeId();
		this.identificationNumber = mergedPatientSearchFilter.getIdentificationNumber();
		this.birthDate = mergedPatientSearchFilter.getBirthDate();
		this.temporary = mergedPatientSearchFilter.getTemporary();
		this.permanentNotValidated = mergedPatientSearchFilter.getPermanentNotValidated();
		this.validated = mergedPatientSearchFilter.getValidated();
		this.permanent = mergedPatientSearchFilter.getPermanent();
	}

	public QueryPart with(){
		String with = " with t as ( mp.active_patient_id, count(mp.active_patient_id) as mergedCount \n" +
				"	from {h-schema}merged_inactive_patient as mip \n" +
				"	join {h-schema}merged_patient as mp on (mip.merged_patient_id = mp.id) \n" +
				"	group by mp.active_patient_id ) \n";
		return new QueryPart(with);
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
				" patient.audit_type_id as auditTypeId , \n" +
				" personExtended.name_self_determination, \n" +
				" t.mergedCount \n";
		return new QueryPart(select);
	}

	public QueryPart from() {
		String from = "	{h-schema}t \n" +
				"	join {h-schema}patient as patient \n" +
				"	join {h-schema}person as person on (patient.person_id = person.id) \n" +
				" 	left join {h-schema}person_extended as personExtended on ( personExtended.person_id = person.id) \n";
		return new QueryPart(from);
	}

	public QueryPart where() {
		List<String> whereString = new ArrayList<>();

		if (firstName != null) {
			firstName = (QueryStringHelper.escapeSql(firstName)).toUpperCase();
			String clause = String.format(LIKE_FORMAT_INDEX, "person.first_name", firstName);
			whereString.add(clause);
		}
		if (middleNames != null) {
			middleNames = (QueryStringHelper.escapeSql(middleNames)).toUpperCase();
			String clause = String.format(LIKE_FORMAT_INDEX, "person.middle_names", middleNames);
			whereString.add(clause);
		}
		if (lastName != null) {
			lastName = (QueryStringHelper.escapeSql(lastName)).toUpperCase();
			String clause = String.format(LIKE_FORMAT_INDEX, "person.last_name", lastName);
			whereString.add(clause);
		}
		if (otherLastNames != null) {
			otherLastNames = (QueryStringHelper.escapeSql(otherLastNames)).toUpperCase();
			String clause = String.format(LIKE_FORMAT_INDEX, "person.other_last_names", otherLastNames);
			whereString.add(clause);
		}
		if (identificationTypeId != null && identificationNumber != null){
			//String identificationTypeString = (QueryStringHelper.escapeSql(identificationTypeId.toString())).toUpperCase();
			String clause = " (person.identification_type_id = "+identificationTypeId+" ) AND (person.identification_number = '" + identificationNumber+"') \n";
			whereString.add(clause);
		}
		if (birthDate != null) {
			String birthDateString = (QueryStringHelper.escapeSql(birthDate.toString())).toUpperCase();
			whereString.add(" (person.birth_date = '"+birthDateString+"') \n");
		}
		if (genderId != null) {
			//String genderString = (QueryStringHelper.escapeSql(genderId.toString())).toUpperCase();
			whereString.add(" (person.gender_id = "+genderId+") \n");
		}

		QueryPart result = new QueryPart(" AND ( ")
				.concatPart(new QueryPart(String.join(" AND ", whereString)))
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

}
