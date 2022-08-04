package net.pladema.patient.repository;

import ar.lamansys.sgx.shared.repositories.QueryStringHelper;
import lombok.NoArgsConstructor;
import net.pladema.patient.controller.dto.PatientSearchFilter;
import net.pladema.patient.service.domain.PatientSearch;
import net.pladema.person.repository.entity.Person;
import ar.lamansys.sgx.shared.repositories.QueryPart;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@NoArgsConstructor
public class PatientSearchQuery {

    // WHERE options
    public static final Integer AND_JOINING_OPERATOR = 1;
    public static final Integer OR_JOINING_OPERATOR = 2;
    public static final Integer LIKE_COMPARATOR = 3;
    public static final Integer EQUAL_COMPARATOR = 4;

    // WHERE clause formats
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
    Boolean filterByNameSelfDetermination;

    private static final String MESSAGE = "No se han encontrado pacientes";

    public PatientSearchQuery(PatientSearchFilter patientSearchFilter){
        this.firstName = patientSearchFilter.getFirstName();
        this.middleNames = patientSearchFilter.getMiddleNames();
        this.lastName = patientSearchFilter.getLastName();
        this.otherLastNames = patientSearchFilter.getOtherLastNames();
        this.genderId = patientSearchFilter.getGenderId();
        this.identificationTypeId = patientSearchFilter.getIdentificationTypeId();
        this.identificationNumber = patientSearchFilter.getIdentificationNumber();
        this.birthDate = patientSearchFilter.getBirthDate();
        this.filterByNameSelfDetermination = patientSearchFilter.getFilterByNameSelfDetermination();
    }

    public QueryPart select() {
    	String select = " patient.id as patientId, \n" +
				" person.id as personId, \n" +
				" person.first_name, \n" +
				" person.middle_names, \n" +
				" person.last_name, \n" +
				" person.other_last_names, \n" +
				" person.gender_id, \n" +
				" person.identification_type_id, \n" +
				" person.identification_number, \n" +
				" person.birth_date, \n" +
				" type.active, \n" +
				" personExtended.name_self_determination \n";


		return new QueryPart(select);
    }

    public QueryPart from() {
    	String from = "	{h-schema}patient as patient \n" +
				"	join {h-schema}person as person on (patient.person_id = person.id) \n" +
				"	join {h-schema}patient_type as type on (patient.type_id = type.id) \n" +
				" 	left join {h-schema}person_extended as personExtended on ( personExtended.person_id = person.id) \n";

    	/*String fromWithPersonExtended = "join {h-schema}person_extended as personExtended on (person.id = personExtended.person_id) \n";
		if (this.filterByNameSelfDetermination)
    		from = from + fromWithPersonExtended;
*/
		return new QueryPart(from);
    }

    public QueryPart whereWithBasicAttributes(Integer joiningOperator) {
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

        String joiningOperatorString = getJoiningOperator(joiningOperator);
        return new QueryPart(String.join(joiningOperatorString, whereString));
    }

    public QueryPart whereWithAllAttributes(Integer joiningOperator, Integer clauseComparator) {
        List<String> whereString = new ArrayList<>();

        if (firstName != null) {
            firstName = (QueryStringHelper.escapeSql(firstName)).toUpperCase();
            String clause = String.format(LIKE_FORMAT_INDEX, "person.first_name", firstName);
            whereString.add(clause);
        }

        return addOtherAttributes(whereString, joiningOperator, clauseComparator);
    }

	public QueryPart whereWithAllAttributesAndNameSelfDetermination(Integer joiningOperator, Integer clauseComparator) {
		List<String> whereString = new ArrayList<>();

		if (firstName != null) {
			firstName = (QueryStringHelper.escapeSql(firstName)).toUpperCase();
			String clause = String.format(LIKE_FORMAT_INDEX, "personExtended.name_self_determination", firstName);
			whereString.add(clause);
		}

		return addOtherAttributes(whereString, joiningOperator, clauseComparator);
	}

	public QueryPart addOtherAttributes (List<String> whereString, Integer joiningOperator, Integer clauseComparator) {

		String clauseFormat = getClauseFormat(clauseComparator);

		if (middleNames != null) {
			middleNames = (QueryStringHelper.escapeSql(middleNames)).toUpperCase();
			String clause = String.format(clauseFormat, "person.middle_names", middleNames);
			whereString.add(clause);
		}
		if (lastName != null) {
			lastName = (QueryStringHelper.escapeSql(lastName)).toUpperCase();
			String clause = String.format(LIKE_FORMAT_INDEX, "person.last_name", lastName);
			whereString.add(clause);
		}
		if (otherLastNames != null) {
			otherLastNames = (QueryStringHelper.escapeSql(otherLastNames)).toUpperCase();
			String clause = String.format(clauseFormat, "person.other_last_names", otherLastNames);
			whereString.add(clause);
		}
		if (genderId != null) {
			whereString.add(" (person.gender_id = '" + genderId + "') \n");
		}
		if (identificationNumber != null){
			String clause = " (person.identification_number = '" + identificationNumber+"')";
			whereString.add(clause);
		}
		if (identificationTypeId != null) {
			whereString.add(" (person.identification_type_id = '"+identificationTypeId+"') \n");
		}
		if (birthDate != null){
			String birthDateString = (QueryStringHelper.escapeSql(birthDate.toString())).toUpperCase();
			whereString.add(" (person.birth_date = '"+birthDateString+"') \n");
		}

		String joiningOperatorString = getJoiningOperator(joiningOperator);
		return new QueryPart(String.join(joiningOperatorString, whereString));
	}

    private String getClauseFormat(Integer clauseComparator) {
        String clauseFormat;
        if (clauseComparator.equals(EQUAL_COMPARATOR))
            clauseFormat = EQUAL_FORMAT;
        else
            clauseFormat = LIKE_FORMAT;
        return clauseFormat;
    }

	public QueryPart addUnion() {
		return new QueryPart("SELECT ")
				.concatPart(this.select())
				.concat(" FROM ")
				.concatPart(this.from())
				.concat(" WHERE ")
				.concatPart(whereWithAllAttributesAndNameSelfDetermination(AND_JOINING_OPERATOR, LIKE_COMPARATOR));
	}

    private String getJoiningOperator(Integer joiningOperator) {
        if (joiningOperator.equals(OR_JOINING_OPERATOR))
            return " OR ";
        else
            return " AND ";
    }

    public String noResultMessage(){
        return MESSAGE;
    }

    public List<PatientSearch> construct(List<Object[]> resultQuery){
        List<PatientSearch> result = new ArrayList<>();

        Map<Integer, List<Object[]>> diagnosisByDocuments = resultQuery
                .stream()
                .collect(Collectors.groupingBy(
                        (Object[] t) -> (Integer)t[0],
                        LinkedHashMap::new,
                        toList())
                );
        diagnosisByDocuments.forEach((id,v) -> {
            Object[] tuple = v.get(0);

			result.add(new PatientSearch(mapPerson(tuple), id, (Boolean) tuple[10], 0, (String) tuple[11])); //rating no se usa, seteo 0

        });
        return result;
    }

    private Person mapPerson(Object[] tuple) {
        int index = 1;
        Integer id = (Integer) tuple[index++];
        String firstNamePerson = (String) tuple[index++];
        String middleNamesPerson = (String) tuple[index++];
        String lastNamePerson = (String) tuple[index++];
        String otherLastNamesPerson = (String) tuple[index++];
        Short genderIdPerson = (Short) tuple[index++];
        Short identificationTypeIdPerson = (Short) tuple[index++];
        String identificationNumberPerson = (String) tuple[index++];
        LocalDate birthDatePerson = tuple[index] != null ? ((Date) tuple[index]).toLocalDate() : null;
        return new Person(id, firstNamePerson, middleNamesPerson, lastNamePerson, otherLastNamesPerson, identificationTypeIdPerson, identificationNumberPerson, genderIdPerson, birthDatePerson);
    }
}