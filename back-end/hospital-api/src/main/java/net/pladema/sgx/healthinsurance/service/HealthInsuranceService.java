package net.pladema.sgx.healthinsurance.service;

import net.pladema.patient.service.domain.MedicalCoveragePlanBo;
import net.pladema.renaper.services.domain.PersonMedicalCoverageBo;

import java.util.Collection;

public interface HealthInsuranceService {

    Collection<PersonMedicalCoverageBo> getAll();
    
    public void addAll(Collection<PersonMedicalCoverageBo> newHealthInsurances);

    PersonMedicalCoverageBo get(Integer rnos);

	Collection<MedicalCoveragePlanBo> getAllPlansByMedicalCoverageId(Integer id);

	MedicalCoveragePlanBo getPlanById(Integer id);
}
