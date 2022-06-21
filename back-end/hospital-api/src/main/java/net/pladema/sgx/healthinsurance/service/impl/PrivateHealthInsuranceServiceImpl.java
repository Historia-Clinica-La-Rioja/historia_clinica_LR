package net.pladema.sgx.healthinsurance.service.impl;

import net.pladema.establishment.repository.MedicalCoveragePlanRepository;
import net.pladema.establishment.repository.entity.MedicalCoveragePlan;
import net.pladema.patient.repository.PrivateHealthInsuranceRepository;
import net.pladema.patient.repository.domain.PrivateHealthInsuranceVo;
import net.pladema.patient.service.domain.MedicalCoveragePlanBo;
import net.pladema.patient.service.domain.PrivateHealthInsuranceBo;
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

    private final MedicalCoveragePlanRepository medicalCoveragePlanRepository;

    public PrivateHealthInsuranceServiceImpl(PrivateHealthInsuranceRepository privateHealthInsuranceRepository,
                                             MedicalCoveragePlanRepository medicalCoveragePlanRepository) {
        super();
        this.privateHealthInsuranceRepository = privateHealthInsuranceRepository;
        this.medicalCoveragePlanRepository = medicalCoveragePlanRepository;
    }

    @Override
    public Collection<PrivateHealthInsuranceBo> getAll() {
        Collection<PrivateHealthInsuranceVo> medicalCoveragedata = privateHealthInsuranceRepository.getAllWithNames(Sort.by(Sort.Direction.ASC, "name"));
        Collection<PrivateHealthInsuranceBo> result = medicalCoveragedata.stream()
                .map(mc -> new PrivateHealthInsuranceBo(mc.getId(), mc.getName(), mc.getCuit(), mc.getType()))
                .collect(Collectors.toList());
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public Collection<MedicalCoveragePlanBo> getAllPlansByMedicalCoverageId(Integer id) {
        Collection<MedicalCoveragePlan> plansData = medicalCoveragePlanRepository.findAllActiveByMedicalCoverageId(id);
        Collection<MedicalCoveragePlanBo> result = plansData.stream()
                .map(plan -> new MedicalCoveragePlanBo(plan.getId(), plan.getMedicalCoverageId(), plan.getPlan()))
                .collect(Collectors.toList());
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public MedicalCoveragePlanBo getPlanById(Integer id) {
        MedicalCoveragePlanBo result = medicalCoveragePlanRepository.findById(id)
                .map(plan -> new MedicalCoveragePlanBo(plan.getId(),plan.getMedicalCoverageId(),plan.getPlan()))
                        .orElseThrow(()->new PrivateHealthInsuranceServiceException(PrivateHealthInsuranceServiceEnumException.PRIVATE_HEALTH_INSURANCE_PLAN_NOT_EXISTS, String.format("El plan con id %s no existe",id)));
        LOG.debug(OUTPUT, result);
        return result;
    }



}
