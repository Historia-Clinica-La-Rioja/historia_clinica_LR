package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.service;

import ar.lamansys.sgh.clinichistory.domain.ips.RiskFactorObservationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.services.ClinicalObservationService;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.NewRiskFactorsObservationDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.RiskFactorObservationDto;
import ar.lamansys.sgh.clinichistory.domain.ips.RiskFactorBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.RiskFactorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RiskFactorExternalServiceImpl implements RiskFactorExternalService {

    private final Logger LOG = LoggerFactory.getLogger(RiskFactorExternalServiceImpl.class);

    private final ClinicalObservationService clinicalObservationService;

    private final RiskFactorMapper riskFactorMapper;

    public RiskFactorExternalServiceImpl(ClinicalObservationService clinicalObservationService,
										 RiskFactorMapper riskFactorMapper) {
        this.clinicalObservationService = clinicalObservationService;
        this.riskFactorMapper = riskFactorMapper;
    }

    @Override
    public NewRiskFactorsObservationDto saveRiskFactors(Integer patientId, NewRiskFactorsObservationDto riskFactorsObservationDto) {
        LOG.debug("Input parameter -> riskFactorsObservationDto {}", riskFactorsObservationDto);
        RiskFactorBo riskFactorBo = riskFactorMapper.fromRiskFactorsObservationDto(riskFactorsObservationDto);
        riskFactorBo = clinicalObservationService.loadRiskFactors(patientId, null, Optional.ofNullable(riskFactorBo));
        NewRiskFactorsObservationDto result = riskFactorMapper.toRiskFactorsObservationDto(riskFactorBo);
        LOG.debug("Output -> {}", result);
        return result;
    }

    @Override
    public RiskFactorObservationDto getRiskFactorObservationById(Integer riskFactorObservationId) {
        LOG.debug("Input parameter -> riskFactorObservationId {}", riskFactorObservationId);
        RiskFactorObservationBo riskFactorObservationBo = clinicalObservationService.getObservationById(riskFactorObservationId);
        RiskFactorObservationDto result = riskFactorMapper.fromRiskFactorObservationBo(riskFactorObservationBo);
        LOG.debug("Output -> result {}", result);
        return result;
    }

	@Override
	public void formatRiskFactors(RiskFactorBo riskFactors) {
		if (riskFactors.getSystolicBloodPressure().getId() == null)
			riskFactors.setSystolicBloodPressure(null);
		if (riskFactors.getDiastolicBloodPressure().getId() == null)
			riskFactors.setDiastolicBloodPressure(null);
		if (riskFactors.getTemperature().getId() == null)
			riskFactors.setTemperature(null);
		if (riskFactors.getHeartRate().getId() == null)
			riskFactors.setHeartRate(null);
		if (riskFactors.getRespiratoryRate().getId() == null)
			riskFactors.setRespiratoryRate(null);
		if (riskFactors.getBloodOxygenSaturation().getId() == null)
			riskFactors.setBloodOxygenSaturation(null);
		if (riskFactors.getBloodGlucose().getId() == null)
			riskFactors.setBloodGlucose(null);
		if (riskFactors.getGlycosylatedHemoglobin().getId() == null)
			riskFactors.setGlycosylatedHemoglobin(null);
		if (riskFactors.getCardiovascularRisk().getId() == null)
			riskFactors.setCardiovascularRisk(null);
	}

}
