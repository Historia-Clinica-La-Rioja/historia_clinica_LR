package ar.lamansys.sgh.shared.infrastructure.input.service;

public interface SharedPersonPort {

	String getCountryIsoCodeFromPerson(Integer personId);

	String getPersonFullNameById(Integer personId);

}
