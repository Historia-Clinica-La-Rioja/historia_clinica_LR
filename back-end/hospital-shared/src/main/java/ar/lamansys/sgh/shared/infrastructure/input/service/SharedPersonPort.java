package ar.lamansys.sgh.shared.infrastructure.input.service;

import ar.lamansys.sgh.shared.domain.general.ContactInfoBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.person.CompletePersonDto;

import java.util.List;

public interface SharedPersonPort {

	String getCountryIsoCodeFromPerson(Integer personId);

	String getPersonFullNameById(Integer personId);

	String getCompletePersonNameById(Integer personId);

	String parseCompletePersonName(String firstName, String middleNames, String lastName, String otherLastNames, String selfDeterminateName);

	String parseCompletePersonName(String givenName, String familyNames, String selfDeterminateName);

	CompletePersonDto getCompletePersonData(Integer personId);

	String getFormalPersonNameById(Integer personId);

	ContactInfoBo getPersonContactInfoById(Integer personId);

    List<String> getCompletePersonsNameByIds(List<Integer> personIds);
}
