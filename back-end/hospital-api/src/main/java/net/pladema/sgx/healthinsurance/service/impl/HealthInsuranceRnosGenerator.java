package net.pladema.sgx.healthinsurance.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.pladema.renaper.services.domain.PersonMedicalCoverageBo;

public class HealthInsuranceRnosGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(HealthInsuranceRnosGenerator.class);
	
	private static final String SUMAR_RNOS = "SUMAR";

    private HealthInsuranceRnosGenerator() {
    }
    
	public static int calculateRnos(PersonMedicalCoverageBo healthInsurance) {
		LOG.debug("Input -> healthInsurance {}", healthInsurance);
		int result;
		if (healthInsurance.getRnos().equals(SUMAR_RNOS)) {
			result = - Math.abs(getHashCode(healthInsurance));
		} else {
			result = Integer.valueOf(healthInsurance.getRnos());
		}
		LOG.debug("Result -> result {}", result);

		return result;
	}

	private static int getHashCode(PersonMedicalCoverageBo healthInsurance) {
		return healthInsurance.getName().hashCode() & 0x0000ffff;
	}
	
}
