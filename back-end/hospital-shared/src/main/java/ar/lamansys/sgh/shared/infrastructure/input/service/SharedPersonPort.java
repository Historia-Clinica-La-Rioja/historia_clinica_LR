package ar.lamansys.sgh.shared.infrastructure.input.service;

public interface SharedPersonPort {

	PersonInfoDto getPersonByIdentificationInfo(String identificationNumber, String identificationType);

	String getCountryIsoCodeFromPerson(Integer personId);

}
