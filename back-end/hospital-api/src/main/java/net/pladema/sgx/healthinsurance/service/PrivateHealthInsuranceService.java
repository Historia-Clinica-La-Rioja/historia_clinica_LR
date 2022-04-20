package net.pladema.sgx.healthinsurance.service;

import net.pladema.patient.service.domain.MedicalCoveragePlanBo;
import net.pladema.patient.service.domain.PrivateHealthInsuranceBo;

import java.util.Collection;

public interface PrivateHealthInsuranceService {

    Collection<PrivateHealthInsuranceBo> getAll();

    Collection<MedicalCoveragePlanBo> getAllPlansByMedicalCoverageId(Integer id);

    MedicalCoveragePlanBo getPlanById(Integer id);
}
