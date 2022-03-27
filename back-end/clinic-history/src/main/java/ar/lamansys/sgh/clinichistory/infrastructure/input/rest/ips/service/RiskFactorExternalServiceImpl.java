package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.service;

import ar.lamansys.sgh.clinichistory.domain.ips.RiskFactorObservationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.services.ClinicalObservationService;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.NewRiskFactorsObservationDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.RiskFactorObservationDto;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.RiskFactorBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.RiskFactorMapper;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPatientPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RiskFactorExternalServiceImpl implements RiskFactorExternalService {

    private final Logger LOG = LoggerFactory.getLogger(RiskFactorExternalServiceImpl.class);

    private final ClinicalObservationService clinicalObservationService;

    private final SharedPatientPort sharedPatientPort;

    private final RiskFactorMapper riskFactorMapper;

    public RiskFactorExternalServiceImpl(ClinicalObservationService clinicalObservationService,
										 SharedPatientPort sharedPatientPort,
										 RiskFactorMapper riskFactorMapper) {
        this.clinicalObservationService = clinicalObservationService;
        this.sharedPatientPort = sharedPatientPort;
        this.riskFactorMapper = riskFactorMapper;
    }

    @Override
    public NewRiskFactorsObservationDto saveRiskFactors(Integer patientId, NewRiskFactorsObservationDto riskFactorsObservationDto) {
        LOG.debug("Input parameter -> riskFactorsObservationDto {}", riskFactorsObservationDto);
        PatientInfoBo patientInfo = getPatientInfoBo(patientId);
        RiskFactorBo riskFactorBo = riskFactorMapper.fromRiskFactorsObservationDto(riskFactorsObservationDto);
        riskFactorBo = clinicalObservationService.loadRiskFactors(patientInfo, null, Optional.ofNullable(riskFactorBo));
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

    private PatientInfoBo getPatientInfoBo(Integer patientId) {
        LOG.debug("Input parameter -> patientId {}", patientId);
        if (patientId == null)
            return new PatientInfoBo();
        BasicPatientDto patientDto = (sharedPatientPort.getBasicDataFromPatient(patientId));
        PatientInfoBo patientInfo = new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge());
        LOG.debug("Output -> {}", patientInfo);
        return patientInfo;
    }
}
