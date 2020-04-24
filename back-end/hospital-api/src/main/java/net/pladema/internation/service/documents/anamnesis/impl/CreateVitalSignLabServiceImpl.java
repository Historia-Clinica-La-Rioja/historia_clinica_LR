package net.pladema.internation.service.documents.anamnesis.impl;

import net.pladema.internation.repository.ips.ObservationLabRepository;
import net.pladema.internation.repository.ips.ObservationVitalSignRepository;
import net.pladema.internation.repository.ips.entity.ObservationLab;
import net.pladema.internation.repository.ips.entity.ObservationVitalSign;
import net.pladema.internation.service.documents.DocumentService;
import net.pladema.internation.service.documents.anamnesis.CreateVitalSignLabService;
import net.pladema.internation.service.domain.ips.AnthropometricDataBo;
import net.pladema.internation.service.domain.ips.ClinicalObservation;
import net.pladema.internation.service.domain.ips.VitalSignBo;
import net.pladema.internation.service.domain.ips.enums.ELab;
import net.pladema.internation.service.domain.ips.enums.EVitalSign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CreateVitalSignLabServiceImpl implements CreateVitalSignLabService {

    public static final String OUTPUT = "Output -> {}";

    private static final Logger LOG = LoggerFactory.getLogger(CreateVitalSignLabServiceImpl.class);

    private final ObservationVitalSignRepository observationVitalSignRepository;

    private final ObservationLabRepository observationLabRepository;

    private final DocumentService documentService;

    public CreateVitalSignLabServiceImpl(ObservationVitalSignRepository observationVitalSignRepository,
                                         ObservationLabRepository observationLabRepository,
                                         DocumentService documentService) {
        this.observationVitalSignRepository = observationVitalSignRepository;
        this.observationLabRepository = observationLabRepository;
        this.documentService = documentService;
    }

    @Override
    public VitalSignBo loadVitalSigns(Integer patientId, Long documentId, Optional<VitalSignBo> optVitalSigns) {
        LOG.debug("Input parameters -> documentId {}, patientId {}, vitalSigns {}", documentId, patientId, optVitalSigns);
        optVitalSigns.ifPresent(vitalSigns -> {

            if(vitalSigns.getSystolicBloodPressure() != null){
                ObservationVitalSign systolicBloodPressure = createObservationVitalSign(patientId,vitalSigns.getSystolicBloodPressure(), EVitalSign.SYSTOLIC_BLOOD_PRESSURE);
                documentService.createDocumentVitalSign(documentId, systolicBloodPressure.getId());
                vitalSigns.setSystolicBloodPressure(createObservationFromVitalSign(systolicBloodPressure));
            }

            if(vitalSigns.getDiastolicBloodPressure() != null) {
                ObservationVitalSign diastolicBloodPressure = createObservationVitalSign(patientId, vitalSigns.getDiastolicBloodPressure(), EVitalSign.DIASTOLIC_BLOOD_PRESSURE);
                documentService.createDocumentVitalSign(documentId, diastolicBloodPressure.getId());
                vitalSigns.setDiastolicBloodPressure(createObservationFromVitalSign(diastolicBloodPressure));
            }

            if(vitalSigns.getMeanPressure() != null) {
                ObservationVitalSign meanPressure = createObservationVitalSign(patientId, vitalSigns.getMeanPressure(), EVitalSign.MEAN_PRESSURE);
                documentService.createDocumentVitalSign(documentId, meanPressure.getId());
                vitalSigns.setMeanPressure(createObservationFromVitalSign(meanPressure));
            }

            if(vitalSigns.getTemperature() != null) {
                ObservationVitalSign temperature = createObservationVitalSign(patientId, vitalSigns.getTemperature(), EVitalSign.TEMPERATURE);
                documentService.createDocumentVitalSign(documentId, temperature.getId());
                vitalSigns.setTemperature(createObservationFromVitalSign(temperature));
            }

            if(vitalSigns.getHeartRate() != null) {
                ObservationVitalSign heartRate = createObservationVitalSign(patientId, vitalSigns.getHeartRate(), EVitalSign.HEART_RATE);
                documentService.createDocumentVitalSign(documentId, heartRate.getId());
                vitalSigns.setHeartRate(createObservationFromVitalSign(heartRate));
            }

            if(vitalSigns.getRespiratoryRate() != null) {
                ObservationVitalSign respiratoryRate = createObservationVitalSign(patientId, vitalSigns.getRespiratoryRate(), EVitalSign.RESPIRATORY_RATE);
                documentService.createDocumentVitalSign(documentId, respiratoryRate.getId());
                vitalSigns.setRespiratoryRate(createObservationFromVitalSign(respiratoryRate));
            }

            if(vitalSigns.getBloodOxygenSaturation() != null) {
                ObservationVitalSign bloodOxygenSaturation = createObservationVitalSign(patientId, vitalSigns.getBloodOxygenSaturation(), EVitalSign.BLOOD_OXYGEN_SATURATION);
                documentService.createDocumentVitalSign(documentId, bloodOxygenSaturation.getId());
                vitalSigns.setBloodOxygenSaturation(createObservationFromVitalSign(bloodOxygenSaturation));
            }
        });
        LOG.debug(OUTPUT, optVitalSigns);
        return optVitalSigns.orElse(null);
    }

    @Override
    public AnthropometricDataBo loadAnthropometricData(Integer patientId, Long documentId, Optional<AnthropometricDataBo> optAnthropometricData) {
        LOG.debug("Input parameters -> documentId {}, patientId {}, anthropometricData {}", documentId, patientId, optAnthropometricData);
        optAnthropometricData.ifPresent(anthropometricData -> {
            if(anthropometricData.getHeight() != null) {
                ObservationVitalSign height = createObservationVitalSign(patientId, anthropometricData.getHeight(), EVitalSign.HEIGHT);
                documentService.createDocumentVitalSign(documentId, height.getId());
                anthropometricData.setHeight(createObservationFromVitalSign(height));
            }

            if(anthropometricData.getWeight() != null) {
                ObservationVitalSign weight = createObservationVitalSign(patientId, anthropometricData.getWeight(), EVitalSign.WEIGHT);
                documentService.createDocumentVitalSign(documentId, weight.getId());
                anthropometricData.setWeight(createObservationFromVitalSign(weight));
            }

            if(anthropometricData.getBloodType() != null) {
                ObservationLab bloodType = createObservationLab(patientId, anthropometricData.getBloodType(), ELab.BLOOD_TYPE);
                documentService.createDocumentLab(documentId, bloodType.getId());
                anthropometricData.setBloodType(createObservationFromLab(bloodType));
            }
        });
        LOG.debug(OUTPUT, optAnthropometricData);
        return optAnthropometricData.orElse(null);
    }

    private ObservationVitalSign createObservationVitalSign(Integer patientId, ClinicalObservation observation, EVitalSign eVitalSign) {
        LOG.debug("Input parameters -> patientId {}, ClinicalObservation {}, eVitalSign {}", patientId, observation, eVitalSign);
        ObservationVitalSign observationVitalSign = observationVitalSignRepository.save(new ObservationVitalSign(patientId, observation.getValue(), eVitalSign, observation.isDeleted()));
        LOG.debug(OUTPUT, observationVitalSign);
        return observationVitalSign;
    }

    private ObservationLab createObservationLab(Integer patientId, ClinicalObservation observation, ELab eLab) {
        LOG.debug("Input parameters -> patientId {}, ClinicalObservation {}, eLab {}", patientId, observation, eLab);
        ObservationLab observationLab = observationLabRepository.save(new ObservationLab(patientId, observation.getValue(), eLab, observation.isDeleted()));
        LOG.debug(OUTPUT, observationLab);
        return observationLab;
    }


    private ClinicalObservation createObservationFromVitalSign(ObservationVitalSign vitalSign) {
        LOG.debug("Input parameters -> VitalSign {}", vitalSign);
        ClinicalObservation result = new ClinicalObservation(vitalSign.getId(), vitalSign.getValue(), vitalSign.isDeleted());
        LOG.debug(OUTPUT, result);
        return result;
    }

    private ClinicalObservation createObservationFromLab(ObservationLab lab) {
        LOG.debug("Input parameters -> ObservationLab {}", lab);
        ClinicalObservation result = new ClinicalObservation(lab.getId(), lab.getValue(), lab.isDeleted());
        LOG.debug(OUTPUT, result);
        return result;
    }

}
