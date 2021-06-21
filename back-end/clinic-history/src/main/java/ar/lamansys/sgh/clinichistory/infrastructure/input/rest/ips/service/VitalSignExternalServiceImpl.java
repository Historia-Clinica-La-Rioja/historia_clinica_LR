package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.service;

import ar.lamansys.sgh.clinichistory.domain.ips.services.ClinicalObservationService;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.NewVitalSignsObservationDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.VitalSignObservationDto;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.VitalSignBo;
import ar.lamansys.sgh.clinichistory.domain.ips.VitalSignObservationBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.VitalSignMapper;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPatientPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VitalSignExternalServiceImpl implements VitalSignExternalService{

    private final Logger LOG = LoggerFactory.getLogger(VitalSignExternalServiceImpl.class);

    private final ClinicalObservationService clinicalObservationService;

    private final SharedPatientPort sharedPatientPort;

    private final VitalSignMapper vitalSignMapper;

    public VitalSignExternalServiceImpl(ClinicalObservationService clinicalObservationService,
                                        SharedPatientPort sharedPatientPort,
                                        VitalSignMapper vitalSignMapper) {
        this.clinicalObservationService = clinicalObservationService;
        this.sharedPatientPort = sharedPatientPort;
        this.vitalSignMapper = vitalSignMapper;
    }

    @Override
    public NewVitalSignsObservationDto saveVitalSigns(Integer patientId, NewVitalSignsObservationDto vitalSignsObservationDto) {
        LOG.debug("Input parameter -> vitalSignsObservationDto {}", vitalSignsObservationDto);
        PatientInfoBo patientInfo = getPatientInfoBo(patientId);
        VitalSignBo vitalSignBo = vitalSignMapper.fromVitalSignsObservationDto(vitalSignsObservationDto);
        vitalSignBo = clinicalObservationService.loadVitalSigns(patientInfo, null, Optional.ofNullable(vitalSignBo));
        NewVitalSignsObservationDto result = vitalSignMapper.toVitalSignsObservationDto(vitalSignBo);
        LOG.debug("Output -> {}", result);
        return result;
    }

    @Override
    public VitalSignObservationDto getVitalSignObservationById(Integer vitalSignObservationId) {
        LOG.debug("Input parameter -> vitalSignObservationId {}", vitalSignObservationId);
        VitalSignObservationBo vitalSignObservationBo = clinicalObservationService.getObservationById(vitalSignObservationId);
        VitalSignObservationDto result = vitalSignMapper.fromVitalSignObservationBo(vitalSignObservationBo);
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
