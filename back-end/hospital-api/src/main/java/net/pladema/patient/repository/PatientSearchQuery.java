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
    public static final Integer OR_JOINING_OPERATOR = 2;

    // WHERE clause formats
	private static final String LIKE_FORMAT_INDEX = " (UPPER(%s) LIKE '%s%%') \n"; //double '%' to escape the character

    String firstName;
    String middleNames;
    String lastName;
    String otherLastNames;
    Short genderId;
    Short identificationTypeId;
    String identificationNumber;
    LocalDate birthDate;
    Boolean filterByNameSelfDetermination;

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
		return new QueryPart(" AND ( ")
				.concatPart(new QueryPart(String.join(joiningOperatorString, whereString)))
				.concat(" )");
    }

    private String getJoiningOperator(Integer joiningOperator) {
        if (joiningOperator.equals(OR_JOINING_OPERATOR))
            return " OR ";
        else
            return " AND ";
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