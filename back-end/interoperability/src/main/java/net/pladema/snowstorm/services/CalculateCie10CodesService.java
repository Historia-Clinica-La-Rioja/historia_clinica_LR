package net.pladema.snowstorm.services;

import net.pladema.snowstorm.services.domain.Cie10RuleFeature;
import net.pladema.snowstorm.services.exceptions.SnowstormApiException;

public interface CalculateCie10CodesService {

    String execute(String sctid, Cie10RuleFeature features) throws SnowstormApiException;

}
