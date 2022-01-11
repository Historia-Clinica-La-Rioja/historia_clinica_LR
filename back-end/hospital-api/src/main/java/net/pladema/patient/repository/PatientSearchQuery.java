package net.pladema.patient.repository;

import ar.lamansys.sgx.shared.repositories.QueryStringHelper;
import lombok.NoArgsConstructor;
import net.pladema.patient.controller.dto.PatientSearchFilter;
import net.pladema.patient.service.domain.PatientSearch;
import net.pladema.person.repository.entity.Person;
import ar.lamansys.sgx.shared.repositories.QueryPart;

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
    private static final String EQUAL_FORMAT = " (UPPER(%s) = '%s') \n";

    String firstName;
    String middleNames;
    String lastName;
    String otherLastNames;
    Short genderId;
    Short identificationTypeId;
    String identificationNumber;
    LocalDate birthDate;

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
    }

    public QueryPart select() {
        return new QueryPart(" patient.id, \n" +
                " person.id, \n" +
                " person.firstName, \n" +
                " person.middleNames, \n" +
                " person.lastName, \n" +
                " person.otherLastNames, \n" +
                " person.genderId, \n" +
                " person.identificationTypeId, \n" +
                " person.identificationNumber, \n" +
                " person.birthDate, \n" +
                " personExtended.nameSelfDetermination, \n" +
                " type.active \n");
    }

    public QueryPart from() {
        return new QueryPart("Patient as patient \n" +
                "join Person as person on (patient.personId = person.id) \n" +
                "join PatientType as type on (patient.typeId = type.id) \n" +
                "join PersonExtended as personExtended on (person.id = personExtended.id) \n"
        );
    }

    public QueryPart whereWithBasicAttributes(Integer joiningOperator, Integer clauseComparator) {
        List<String> whereString = new ArrayList<>();

        String clauseFormat = getClauseFormat(clauseComparator);

        if (firstName != null) {
            firstName = (QueryStringHelper.escapeSql(firstName)).toUpperCase();
            String clause = String.format(clauseFormat, "person.firstName", firstName);
            whereString.add(clause);
        }
        if (lastName != null) {
            lastName = (QueryStringHelper.escapeSql(lastName)).toUpperCase();
            String clause = String.format(clauseFormat, "person.lastName", lastName);
            whereString.add(clause);
        }
        if (identificationNumber != null){
            identificationNumber = (QueryStringHelper.escapeSql(identificationNumber)).toUpperCase();
            String clause = String.format(clauseFormat, "person.identificationNumber", identificationNumber);
            whereString.add(clause);
        }
        if(birthDate != null){
            String birthDateString = (QueryStringHelper.escapeSql(birthDate.toString())).toUpperCase();
            whereString.add(" (person.birthDate = '"+birthDateString+"') \n");
        }

        String joiningOperatorString = getJoiningOperator(joiningOperator);
        return new QueryPart(String.join(joiningOperatorString, whereString));
    }

    public QueryPart whereWithAllAttributes(Integer joiningOperator, Integer clauseComparator) {
        List<String> whereString = new ArrayList<>();

        String clauseFormat = getClauseFormat(clauseComparator);

        if (firstName != null) {
            firstName = (QueryStringHelper.escapeSql(firstName)).toUpperCase();
            String clause = String.format(clauseFormat, "person.firstName", firstName);
            whereString.add(clause);
        }
        if (middleNames != null) {
            middleNames = (QueryStringHelper.escapeSql(middleNames)).toUpperCase();
            String clause = String.format(clauseFormat, "person.middleNames", middleNames);
            whereString.add(clause);
        }
        if (lastName != null) {
            lastName = (QueryStringHelper.escapeSql(lastName)).toUpperCase();
            String clause = String.format(clauseFormat, "person.lastName", lastName);
            whereString.add(clause);
        }
        if (otherLastNames != null) {
            otherLastNames = (QueryStringHelper.escapeSql(otherLastNames)).toUpperCase();
            String clause = String.format(clauseFormat, "person.otherLastNames", otherLastNames);
            whereString.add(clause);
        }
        if (genderId != null) {
            whereString.add(" (person.genderId = '" + genderId + "') \n");
        }
        if (identificationNumber != null){
            identificationNumber = (QueryStringHelper.escapeSql(identificationNumber)).toUpperCase();
            String clause = String.format(clauseFormat, "person.identificationNumber", identificationNumber);
            whereString.add(clause);
        }
        if (identificationTypeId != null) {
            whereString.add(" (person.identificationTypeId = '"+identificationTypeId+"') \n");
        }
        if(birthDate != null){
            String birthDateString = (QueryStringHelper.escapeSql(birthDate.toString())).toUpperCase();
            whereString.add(" (person.birthDate = '"+birthDateString+"') \n");
        }

        String joiningOperatorString = getJoiningOperator(joiningOperator);
        return new QueryPart(String.join(joiningOperatorString, whereString));
    }

    public QueryPart whereWithNameSelfDeterminationAttribute(Integer clauseComparator) {
        String clauseFormat = getClauseFormat(clauseComparator);
        firstName = (QueryStringHelper.escapeSql(firstName)).toUpperCase();
        String clause = String.format(clauseFormat, "personExtended.nameSelfDetermination", firstName);
        return new QueryPart(clause);
    }

    private String getClauseFormat(Integer clauseComparator) {
        String clauseFormat;
        if (clauseComparator.equals(EQUAL_COMPARATOR))
            clauseFormat = EQUAL_FORMAT;
        else
            clauseFormat = LIKE_FORMAT;
        return clauseFormat;
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
            result.add(new PatientSearch(mapPerson(tuple), id, (Boolean) tuple[11], 0, (String) tuple[10])); //rating no se usa, seteo 0
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
        LocalDate birthDatePerson = (LocalDate) tuple[index];
        return new Person(id, firstNamePerson, middleNamesPerson, lastNamePerson, otherLastNamesPerson, identificationTypeIdPerson, identificationNumberPerson, genderIdPerson, birthDatePerson);
    }
}