package net.pladema.snowstorm.services;

import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.exception.RestTemplateApiException;
import net.pladema.snowstorm.services.domain.Cie10RuleFeature;

public interface CalculateCie10CodesService {

    String execute(String sctid, Cie10RuleFeature features) throws RestTemplateApiException;

}
