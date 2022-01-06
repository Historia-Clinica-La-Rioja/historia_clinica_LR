package net.pladema.sgx.healthinsurance.service.impl;

import net.pladema.patient.repository.MedicalCoverageRepository;
import net.pladema.patient.repository.PrivateHealthInsuranceRepository;
import net.pladema.patient.repository.domain.PrivateHealthInsuranceVo;
import net.pladema.patient.service.domain.PrivateHealthInsuranceBo;
import net.pladema.sgx.healthinsurance.service.PrivateHealthInsuranceService;
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

    public PrivateHealthInsuranceServiceImpl(PrivateHealthInsuranceRepository privateHealthInsuranceRepository, MedicalCoverageRepository medicalCoverageRepository) {
        super();
        this.privateHealthInsuranceRepository = privateHealthInsuranceRepository;
    }

    @Override
    public Collection<PrivateHealthInsuranceBo> getAll() {
        Collection<PrivateHealthInsuranceVo> medicalCoveragedata = privateHealthInsuranceRepository.getAllWithNames(Sort.by(Sort.Direction.ASC, "name"));
        Collection<PrivateHealthInsuranceBo> result = medicalCoveragedata.stream()
                .map(mc -> new PrivateHealthInsuranceBo(mc.getId(), mc.getName(), mc.getCuit(), mc.getPlan()))
                .collect(Collectors.toList());
        LOG.debug(OUTPUT, result);
        return result;
    }

}
