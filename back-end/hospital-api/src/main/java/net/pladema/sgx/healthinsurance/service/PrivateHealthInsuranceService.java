package net.pladema.sgx.healthinsurance.service;

import net.pladema.patient.service.domain.PrivateHealthInsuranceBo;
import net.pladema.patient.service.domain.PrivateHealthInsurancePlanBo;

import java.util.Collection;

public interface PrivateHealthInsuranceService {

    Collection<PrivateHealthInsuranceBo> getAll();

    Collection<PrivateHealthInsurancePlanBo> getAllPlansById(Integer id);

    PrivateHealthInsurancePlanBo getPlanById(Integer id);
}
