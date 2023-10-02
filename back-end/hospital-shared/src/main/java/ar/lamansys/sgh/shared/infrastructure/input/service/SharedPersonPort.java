package ar.lamansys.sgh.shared.infrastructure.input.service;

public interface SharedPersonPort {

	String getCountryIsoCodeFromPerson(Integer personId);

	String getPersonFullNameById(Integer personId);

	String getCompletePersonNameById(Integer personId);

	String parseCompletePersonName(String firstName, String middleNames, String lastName, String otherLastNames, String selfDeterminateName);

}
