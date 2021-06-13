package net.pladema.snowstorm.services;

import net.pladema.snowstorm.services.domain.Cie10RuleFeature;

public interface CalculateCie10CodesService {

    String execute(String sctid, Cie10RuleFeature features);

}
