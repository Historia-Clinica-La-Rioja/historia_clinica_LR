package net.pladema.clinichistory.documents.controller.service;

import net.pladema.clinichistory.documents.controller.dto.NewVitalSignsObservationDto;
import net.pladema.clinichistory.documents.controller.dto.VitalSignObservationDto;
import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.documents.service.ips.ClinicalObservationService;
import ar.lamansys.sgh.clinichistory.domain.ips.VitalSignBo;
import ar.lamansys.sgh.clinichistory.domain.ips.VitalSignObservationBo;
import net.pladema.clinichistory.hospitalization.controller.generalstate.mapper.VitalSignMapper;
import net.pladema.patient.controller.dto.BasicPatientDto;
import net.pladema.patient.controller.service.PatientExternalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VitalSignExternalServiceImpl implements VitalSignExternalService{

    private final Logger LOG = LoggerFactory.getLogger(VitalSignExternalServiceImpl.class);

    private final ClinicalObservationService clinicalObservationService;

    private final PatientExternalService patientExternalService;

    private final VitalSignMapper vitalSignMapper;

    public VitalSignExternalServiceImpl(ClinicalObservationService clinicalObservationService,
                                        PatientExternalService patientExternalService,
                                        VitalSignMapper vitalSignMapper) {
        this.clinicalObservationService = clinicalObservationService;
        this.patientExternalService = patientExternalService;
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
        BasicPatientDto patientDto = (patientExternalService.getBasicDataFromPatient(patientId));
        PatientInfoBo patientInfo = new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge());
        LOG.debug("Output -> {}", patientInfo);
        return patientInfo;
    }
}
