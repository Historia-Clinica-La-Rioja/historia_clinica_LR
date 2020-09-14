package net.pladema.sgx.healthinsurance.service;

import net.pladema.renaper.services.domain.PersonMedicalCoverageBo;

import java.util.Collection;

public interface HealthInsuranceService {

    Collection<PersonMedicalCoverageBo> getAll();
    
    public void addAll(Collection<PersonMedicalCoverageBo> newHealthInsurances);

    PersonMedicalCoverageBo get(Integer rnos);
}
