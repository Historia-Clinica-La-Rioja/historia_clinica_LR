package ar.lamansys.sgh.shared.infrastructure.input.service;

import ar.lamansys.sgh.shared.domain.general.ContactInfoBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.person.CompletePersonDto;

public interface SharedPersonPort {

	String getCountryIsoCodeFromPerson(Integer personId);

	String getPersonFullNameById(Integer personId);

	String getCompletePersonNameById(Integer personId);

	String parseCompletePersonName(String firstName, String middleNames, String lastName, String otherLastNames, String selfDeterminateName);
	
	CompletePersonDto getCompletePersonData(Integer personId);

	ContactInfoBo getPersonContactInfoById(Integer personId);

}
