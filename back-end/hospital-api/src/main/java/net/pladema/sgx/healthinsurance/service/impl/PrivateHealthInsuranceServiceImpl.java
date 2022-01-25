package net.pladema.sgx.healthinsurance.service.impl;

import net.pladema.establishment.repository.PrivateHealthInsurancePlanRepository;
import net.pladema.establishment.repository.entity.PrivateHealthInsurancePlan;
import net.pladema.patient.repository.MedicalCoverageRepository;
import net.pladema.patient.repository.PrivateHealthInsuranceRepository;
import net.pladema.patient.repository.domain.PrivateHealthInsuranceVo;
import net.pladema.patient.service.domain.PrivateHealthInsuranceBo;
import net.pladema.patient.service.domain.PrivateHealthInsurancePlanBo;
import net.pladema.sgx.healthinsurance.service.PrivateHealthInsuranceService;
import net.pladema.sgx.healthinsurance.service.exceptions.PrivateHealthInsuranceServiceEnumException;
import net.pladema.sgx.healthinsurance.service.exceptions.PrivateHealthInsuranceServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class PrivateHealthInsuranceServiceImpl implements PrivateHealthInsuranceService {

    private static final Logger LOG = LoggerFactory.getLogger(PrivateHealthInsuranceServiceImpl.class);

    private static final String OUTPUT = "output -> {}";

    private final PrivateHealthInsuranceRepository privateHealthInsuranceRepository;

    private final PrivateHealthInsurancePlanRepository privateHealthInsurancePlanRepository;

    public PrivateHealthInsuranceServiceImpl(PrivateHealthInsuranceRepository privateHealthInsuranceRepository,
                                             PrivateHealthInsurancePlanRepository privateHealthInsurancePlanRepository) {
        super();
        this.privateHealthInsuranceRepository = privateHealthInsuranceRepository;
        this.privateHealthInsurancePlanRepository = privateHealthInsurancePlanRepository;
    }

    @Override
    public Collection<PrivateHealthInsuranceBo> getAll() {
        Collection<PrivateHealthInsuranceVo> medicalCoveragedata = privateHealthInsuranceRepository.getAllWithNames(Sort.by(Sort.Direction.ASC, "name"));
        Collection<PrivateHealthInsuranceBo> result = medicalCoveragedata.stream()
                .map(mc -> new PrivateHealthInsuranceBo(mc.getId(), mc.getName(), mc.getCuit()))
                .collect(Collectors.toList());
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public Collection<PrivateHealthInsurancePlanBo> getAllPlansById(Integer id) {
        Collection<PrivateHealthInsurancePlan> plansData = privateHealthInsurancePlanRepository.findByPrivateHealthInsuranceId(id);
        Collection<PrivateHealthInsurancePlanBo> result = plansData.stream()
                .map(plan -> new PrivateHealthInsurancePlanBo(plan.getId(), plan.getPrivateHealthInsuranceId(), plan.getPlan()))
                .collect(Collectors.toList());
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public PrivateHealthInsurancePlanBo getPlanById(Integer id) {
        PrivateHealthInsurancePlanBo result = privateHealthInsurancePlanRepository.findById(id)
                .map(plan -> new PrivateHealthInsurancePlanBo(plan.getId(),plan.getPrivateHealthInsuranceId(),plan.getPlan()))
                        .orElseThrow(()->new PrivateHealthInsuranceServiceException(PrivateHealthInsuranceServiceEnumException.PRIVATE_HEALTH_INSURANCE_PLAN_NOT_EXISTS, String.format("El plan con id %s no existe",id)));
        LOG.debug(OUTPUT, result);
        return result;
    }



}
