package net.pladema.sgx.healthinsurance.service.impl;

import net.pladema.patient.repository.domain.HealthInsuranceVo;
import net.pladema.patient.repository.entity.MedicalCoverage;
import net.pladema.person.repository.HealthInsuranceRepository;
import net.pladema.patient.repository.MedicalCoverageRepository;
import net.pladema.person.repository.entity.HealthInsurance;
import net.pladema.renaper.services.domain.PersonMedicalCoverageBo;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import net.pladema.sgx.healthinsurance.service.HealthInsuranceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

import static net.pladema.sgx.healthinsurance.service.impl.HealthInsuranceRnosGenerator.calculateRnos;

@Service
public class HealthInsuranceServiceImpl implements HealthInsuranceService {


	private static final Logger LOG = LoggerFactory.getLogger(HealthInsuranceServiceImpl.class);

    private static final String OUTPUT = "output -> {}";

    private static final String WRONG_RNOS = "wrong rnos";

    private static final String HEALTH_INSURANCE_NOT_FOUND ="health-insurance.not.found";

    private final HealthInsuranceRepository healthInsuranceRepository;

    private final MedicalCoverageRepository medicalCoverageRepository;

    public HealthInsuranceServiceImpl(HealthInsuranceRepository healthInsuranceRepository, MedicalCoverageRepository medicalCoverageRepository){
        super();
        this.healthInsuranceRepository = healthInsuranceRepository;
        this.medicalCoverageRepository = medicalCoverageRepository;
    }

    @Override
    public Collection<PersonMedicalCoverageBo> getAll() {
        Collection<HealthInsuranceVo> medicalCoveragedata = healthInsuranceRepository.getAllWithNames(Sort.by(Sort.Direction.ASC, "name"));
        Collection<PersonMedicalCoverageBo> result = medicalCoveragedata.stream()
                .map(PersonMedicalCoverageBo::new)
                .collect(Collectors.toList());
        LOG.debug(OUTPUT, result);
        return result;
    }
    
	@Override
	public void addAll(Collection<PersonMedicalCoverageBo> newHealthInsurances) {
		LOG.debug("Input-> newHealthInsurances {}", newHealthInsurances);
		newHealthInsurances.stream().filter(hi -> !healthInsuranceRepository.existsByRnos(calculateRnos(hi)))
				.forEach(hi -> {
				    MedicalCoverage saved = medicalCoverageRepository
                            .save(new MedicalCoverage(hi.getName()));
					healthInsuranceRepository
							.save(new HealthInsurance(saved.getId(), saved.getName(), calculateRnos(hi), hi.getAcronym()));
					LOG.debug("HealthInsurance Added-> newHealthInsurance {}", hi);
				});
	}

    @Override
    public PersonMedicalCoverageBo get(Integer rnos) {
        HealthInsuranceVo healthInsuranceOptional = healthInsuranceRepository.findByRnos(rnos).orElseThrow(() -> new NotFoundException(WRONG_RNOS, HEALTH_INSURANCE_NOT_FOUND));
        PersonMedicalCoverageBo result = new PersonMedicalCoverageBo(healthInsuranceOptional);
        return result;
    }
}
