package net.pladema.sgx.healthinsurance.service.impl;

import net.pladema.establishment.repository.MedicalCoveragePlanRepository;
import net.pladema.establishment.repository.entity.MedicalCoveragePlan;
import net.pladema.patient.controller.dto.EMedicalCoverageType;
import net.pladema.patient.repository.domain.HealthInsuranceVo;
import net.pladema.patient.service.domain.MedicalCoveragePlanBo;
import net.pladema.person.repository.HealthInsuranceRepository;
import net.pladema.patient.repository.MedicalCoverageRepository;
import net.pladema.person.repository.entity.HealthInsurance;
import net.pladema.renaper.services.domain.PersonMedicalCoverageBo;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import net.pladema.sgx.healthinsurance.service.HealthInsuranceService;
import net.pladema.sgx.healthinsurance.service.exceptions.PrivateHealthInsuranceServiceEnumException;
import net.pladema.sgx.healthinsurance.service.exceptions.PrivateHealthInsuranceServiceException;

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

	private final MedicalCoveragePlanRepository medicalCoveragePlanRepository;

	public HealthInsuranceServiceImpl(HealthInsuranceRepository healthInsuranceRepository,
									  MedicalCoverageRepository medicalCoverageRepository,
									  MedicalCoveragePlanRepository medicalCoveragePlanRepository){
        super();
        this.healthInsuranceRepository = healthInsuranceRepository;
        this.medicalCoverageRepository = medicalCoverageRepository;
		this.medicalCoveragePlanRepository = medicalCoveragePlanRepository;
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
		newHealthInsurances.forEach(nhi -> healthInsuranceRepository.findByRnos(calculateRnos(nhi)).ifPresentOrElse(hi -> {
			if (hi.isDeleted()) {
				hi.setDeleted(false);
				healthInsuranceRepository.save(hi);
			}
		}, () -> {
			healthInsuranceRepository.save(new HealthInsurance(null, nhi.getName(), nhi.getCuit(), calculateRnos(nhi), nhi.getAcronym(), EMedicalCoverageType.OBRASOCIAL.getId()));
			LOG.debug("HealthInsurance Added-> newHealthInsurance {}", nhi);
		}));
	}

    @Override
    public PersonMedicalCoverageBo get(Integer rnos) {
        HealthInsurance healthInsuranceOptional = healthInsuranceRepository.findByRnos(rnos).orElseThrow(() -> new NotFoundException(WRONG_RNOS, HEALTH_INSURANCE_NOT_FOUND));
        PersonMedicalCoverageBo result = new PersonMedicalCoverageBo(healthInsuranceOptional);
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
