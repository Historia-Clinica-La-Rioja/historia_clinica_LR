package net.pladema.patient.repository;

import lombok.NoArgsConstructor;
import net.pladema.patient.controller.dto.PatientSearchFilter;
import net.pladema.patient.service.StringHelper;
import net.pladema.patient.service.domain.PatientSearch;
import net.pladema.person.repository.entity.Person;
import net.pladema.sgx.repository.QueryPart;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@NoArgsConstructor
public class PatientSearchQuery {

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
                " type.active \n");
    }

    public QueryPart from() {
        return new QueryPart("Patient as patient \n" +
                "join Person as person on (patient.personId = person.id) \n" +
                "join PatientType as type on (patient.typeId = type.id) \n"
        );
    }

    public QueryPart where() {
        List<String> whereString = new ArrayList<>();
        if (firstName != null) {
            firstName = (StringHelper.escapeSql(firstName)).toUpperCase();
            whereString.add(" (UPPER(person.firstName) LIKE '%" + firstName + "%') \n");
        }
        if (middleNames != null) {
            middleNames = (StringHelper.escapeSql(middleNames)).toUpperCase();
            whereString.add(" (UPPER(person.middleNames) LIKE '%" + middleNames + "%') \n");
        }
        if (lastName != null) {
            lastName = (StringHelper.escapeSql(lastName)).toUpperCase();
            whereString.add(" (UPPER(person.lastName) LIKE '%" + lastName + "%') \n");
        }
        if (otherLastNames != null) {
            otherLastNames = (StringHelper.escapeSql(otherLastNames)).toUpperCase();
            whereString.add(" (UPPER(person.otherLastNames) LIKE '%" + otherLastNames + "%') \n");
        }
        if (genderId != null) {
            whereString.add(" (person.genderId = '" + genderId + "') \n");
        }
        if (identificationNumber != null){
            identificationNumber = (StringHelper.escapeSql(identificationNumber)).toUpperCase();
            whereString.add(" (UPPER(person.identificationNumber) LIKE '%"+identificationNumber+"%') \n");
        }
        if (identificationTypeId != null) {
            whereString.add(" (person.identificationTypeId = '"+identificationTypeId+"') \n");
        }
        if(birthDate != null){
            String birthDateString = (StringHelper.escapeSql(birthDate.toString())).toUpperCase();
            whereString.add(" (person.birthDate = '"+birthDateString+"') \n");
        }
        return new QueryPart(String.join(" AND ", whereString));
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
            result.add(new PatientSearch(mapPerson(tuple), id, (Boolean) tuple[10], 0)); //rating no se usa, seteo 0
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