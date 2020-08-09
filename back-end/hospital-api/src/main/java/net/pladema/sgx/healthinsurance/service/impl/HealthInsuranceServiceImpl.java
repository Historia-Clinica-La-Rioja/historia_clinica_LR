package net.pladema.sgx.healthinsurance.service.impl;

import net.pladema.person.repository.HealthInsuranceRepository;
import net.pladema.person.repository.entity.HealthInsurance;
import net.pladema.renaper.services.domain.PersonMedicalCoverageBo;
import net.pladema.sgx.healthinsurance.service.HealthInsuranceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class HealthInsuranceServiceImpl implements HealthInsuranceService {

    private static final Logger LOG = LoggerFactory.getLogger(HealthInsuranceServiceImpl.class);

    private static final String OUTPUT = "output -> {}";

    private final HealthInsuranceRepository healthInsuranceRepository;

    public HealthInsuranceServiceImpl(HealthInsuranceRepository healthInsuranceRepository){
        super();
        this.healthInsuranceRepository = healthInsuranceRepository;
    }

    @Override
    public Collection<PersonMedicalCoverageBo> getAll() {
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        Collection<HealthInsurance> medicalCoveragedata = healthInsuranceRepository.findAll(sort);
        Collection<PersonMedicalCoverageBo> result = medicalCoveragedata.stream()
                .map(PersonMedicalCoverageBo::new)
                .collect(Collectors.toList());
        LOG.debug(OUTPUT, result);
        return result;
    }
}
