package ar.lamansys.sgh.shared.infrastructure.input.service;

import ar.lamansys.sgh.shared.domain.general.ContactInfoBo;

public interface SharedPersonPort {

	String getCountryIsoCodeFromPerson(Integer personId);

	String getPersonFullNameById(Integer personId);

	String getCompletePersonNameById(Integer personId);

	String parseCompletePersonName(String firstName, String middleNames, String lastName, String otherLastNames, String selfDeterminateName);

	ContactInfoBo getPersonContactInfoById(Integer personId);

}
