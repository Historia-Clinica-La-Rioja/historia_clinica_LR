package ar.lamansys.sgh.clinichistory.application.fetchHCE;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.HCEAllergyIntoleranceRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity.HCEAllergyVo;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEAllergyBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedHospitalizationPort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HCEAllergyServiceImpl implements HCEAllergyService {

    private static final Logger LOG = LoggerFactory.getLogger(HCEAllergyServiceImpl.class);

    private static final String LOGGING_OUTPUT = "Output -> {}";
    private static final String LOGGING_INPUT = "Input parameters -> patientId {} ";

    private final HCEAllergyIntoleranceRepository hceAllergyIntoleranceRepository;

	private final SharedHospitalizationPort sharedHospitalizationPort;


    public HCEAllergyServiceImpl(HCEAllergyIntoleranceRepository hceAllergyIntoleranceRepository,
								 SharedHospitalizationPort sharedHospitalizationPort) {
        this.hceAllergyIntoleranceRepository = hceAllergyIntoleranceRepository;
		this.sharedHospitalizationPort = sharedHospitalizationPort;
	}

    @Override
    public List<HCEAllergyBo> getAllergies(Integer patientId) {
        LOG.debug(LOGGING_INPUT, patientId);
        List<HCEAllergyVo> resultQuery = hceAllergyIntoleranceRepository.findActiveAllergiesByPatient(patientId);
        List<HCEAllergyBo> result = resultQuery.stream().map(HCEAllergyBo::new).collect(Collectors.toList());
        LOG.debug(LOGGING_OUTPUT, result);
        return result;
    }

	@Override
	public List<HCEAllergyBo> getActiveInternmentEpisodeAllergies(Integer institutionId, Integer patientId) {
		LOG.debug(LOGGING_INPUT, patientId);
		List<HCEAllergyVo> allergies = sharedHospitalizationPort.getInternmentEpisodeId(institutionId, patientId)
				.map(ie -> sharedHospitalizationPort.getInternmentEpisodeAllergies(ie)
						.stream().map(hceAllergyIntoleranceRepository::findHCEAllergy)
						.collect(Collectors.toList()))
				.orElse(null);
		List<HCEAllergyBo> result = new ArrayList<>();
		if (allergies != null) result = allergies.stream().map(HCEAllergyBo::new).collect(Collectors.toList());
		LOG.debug(LOGGING_OUTPUT, result);
		return result;
	}


}