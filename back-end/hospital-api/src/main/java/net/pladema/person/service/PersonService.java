package net.pladema.person.service;

import ar.lamansys.sgh.shared.domain.general.ContactInfoBo;
import net.pladema.patient.controller.dto.AuditPatientSearch;
import net.pladema.person.repository.domain.CompletePersonNameBo;
import net.pladema.person.repository.domain.CompletePersonVo;
import net.pladema.person.repository.domain.DuplicatePersonVo;
import net.pladema.person.repository.domain.PersonSearchResultVo;
import net.pladema.person.repository.domain.PersonalInformation;
import net.pladema.person.repository.entity.Person;
import net.pladema.person.repository.entity.PersonExtended;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PersonService {

    Person addPerson(Person person);

    Person getPerson(Integer id);

    Optional<Person> findPerson(Integer id);

    List<Person> getPeople(Set<Integer> personIds);

    PersonExtended addPersonExtended(PersonExtended person);
    
    PersonExtended getPersonExtended(Integer personId);

    List<Integer> getPersonByDniAndGender(Short identificationTypeId, String identificationNumber, Short genderId);

    Optional<PersonalInformation> getPersonalInformation(Integer personId);
    
    Optional<CompletePersonVo> getCompletePerson(Integer personId);

	String getCountryIsoCodeFromPerson(Integer personId);

	List<DuplicatePersonVo> getDuplicatePersonsByFilter(AuditPatientSearch auditPatientSearch);
    
	Optional<Person> findByPatientId(Integer patientId);
    
	List<PersonSearchResultVo> getPatientsPersonalInfo(DuplicatePersonVo duplicatePersonVo);

	Optional<CompletePersonNameBo> findByHealthcareProfessionalPersonDataByDiaryId(Integer diaryId);

	String getCompletePersonNameById(Integer personId);

    String getFormalPersonNameById(Integer personId);

    String parseCompletePersonName(String firstName, String middleNames, String lastName, String otherLastNames, String selfDeterminateName);

    String parseFormalPersonName(String firstName, String middleNames, String lastName, String otherLastNames, String selfDeterminateName);

    ContactInfoBo getContactInfoById(Integer personId);

    List<String> getCompletePersonNameByIds(List<Integer> personIds);
}
