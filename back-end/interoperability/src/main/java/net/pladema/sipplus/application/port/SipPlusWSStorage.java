package net.pladema.sipplus.application.port;

import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.exception.RestTemplateApiException;

import java.util.List;

public interface SipPlusWSStorage {

	List<Integer> getPregnancies(Integer patientId) throws RestTemplateApiException;

	void createPregnancy(Integer patientId, Integer pregnancyNumber);

}
