package net.pladema.clinichistory.documents.core.ips;

import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.documents.repository.ips.ObservationLabRepository;
import net.pladema.clinichistory.documents.repository.ips.ObservationVitalSignRepository;
import net.pladema.clinichistory.documents.repository.ips.entity.ObservationLab;
import net.pladema.clinichistory.documents.repository.ips.entity.ObservationVitalSign;
import net.pladema.clinichistory.documents.service.ips.ClinicalObservationService;
import net.pladema.clinichistory.documents.service.ips.domain.AnthropometricDataBo;
import net.pladema.clinichistory.documents.service.ips.domain.ClinicalObservationBo;
import net.pladema.clinichistory.documents.service.ips.domain.VitalSignBo;
import net.pladema.clinichistory.documents.service.ips.domain.enums.EObservationLab;
import net.pladema.clinichistory.documents.service.ips.domain.enums.EVitalSign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClinicalObservationServiceImpl implements ClinicalObservationService {

    private static final Logger LOG = LoggerFactory.getLogger(ClinicalObservationServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final ObservationVitalSignRepository observationVitalSignRepository;

    private final ObservationLabRepository observationLabRepository;

    private final DocumentService documentService;

    public ClinicalObservationServiceImpl(ObservationVitalSignRepository observationVitalSignRepository,
                                          ObservationLabRepository observationLabRepository,
                                          DocumentService documentService) {
        this.observationVitalSignRepository = observationVitalSignRepository;
        this.observationLabRepository = observationLabRepository;
        this.documentService = documentService;
    }

    @Override
    public VitalSignBo loadVitalSigns(Integer patientId, Long documentId, Optional<VitalSignBo> optVitalSigns) {
        LOG.debug("Input parameters -> documentId {}, patientId {}, optVitalSigns {}", documentId, patientId, optVitalSigns);
        optVitalSigns.ifPresent(vitalSign -> {
            if(mustSaveClinicalObservation(vitalSign.getSystolicBloodPressure())){
                ObservationVitalSign systolicBloodPressure = createObservationVitalSign(patientId,
                        vitalSign.getSystolicBloodPressure(), EVitalSign.SYSTOLIC_BLOOD_PRESSURE);
                documentService.createDocumentVitalSign(documentId, systolicBloodPressure.getId());
                vitalSign.setSystolicBloodPressure(createObservationFromVitalSign(systolicBloodPressure));
            }

            if(mustSaveClinicalObservation(vitalSign.getDiastolicBloodPressure())) {
                ObservationVitalSign diastolicBloodPressure = createObservationVitalSign(patientId,
                        vitalSign.getDiastolicBloodPressure(), EVitalSign.DIASTOLIC_BLOOD_PRESSURE);
                documentService.createDocumentVitalSign(documentId, diastolicBloodPressure.getId());
                vitalSign.setDiastolicBloodPressure(createObservationFromVitalSign(diastolicBloodPressure));
            }

            if(mustSaveClinicalObservation(vitalSign.getMeanPressure())) {
                ObservationVitalSign meanPressure = createObservationVitalSign(patientId,
                        vitalSign.getMeanPressure(), EVitalSign.MEAN_PRESSURE);
                documentService.createDocumentVitalSign(documentId, meanPressure.getId());
                vitalSign.setMeanPressure(createObservationFromVitalSign(meanPressure));
            }

            if(mustSaveClinicalObservation(vitalSign.getTemperature())) {
                ObservationVitalSign temperature = createObservationVitalSign(patientId,
                        vitalSign.getTemperature(), EVitalSign.TEMPERATURE);
                documentService.createDocumentVitalSign(documentId, temperature.getId());
                vitalSign.setTemperature(createObservationFromVitalSign(temperature));
            }

            if(mustSaveClinicalObservation(vitalSign.getHeartRate())) {
                ObservationVitalSign heartRate = createObservationVitalSign(patientId,
                        vitalSign.getHeartRate(), EVitalSign.HEART_RATE);
                documentService.createDocumentVitalSign(documentId, heartRate.getId());
                vitalSign.setHeartRate(createObservationFromVitalSign(heartRate));
            }

            if(mustSaveClinicalObservation(vitalSign.getRespiratoryRate())) {
                ObservationVitalSign respiratoryRate = createObservationVitalSign(patientId,
                        vitalSign.getRespiratoryRate(), EVitalSign.RESPIRATORY_RATE);
                documentService.createDocumentVitalSign(documentId, respiratoryRate.getId());
                vitalSign.setRespiratoryRate(createObservationFromVitalSign(respiratoryRate));
            }

            if(mustSaveClinicalObservation(vitalSign.getBloodOxygenSaturation())) {
                ObservationVitalSign bloodOxygenSaturation = createObservationVitalSign(patientId,
                        vitalSign.getBloodOxygenSaturation(), EVitalSign.BLOOD_OXYGEN_SATURATION);
                documentService.createDocumentVitalSign(documentId, bloodOxygenSaturation.getId());
                vitalSign.setBloodOxygenSaturation(createObservationFromVitalSign(bloodOxygenSaturation));
            }
        });
        LOG.debug(OUTPUT, optVitalSigns);
        return optVitalSigns.orElse(null);
    }

    @Override
    public AnthropometricDataBo loadAnthropometricData(Integer patientId, Long documentId, Optional<AnthropometricDataBo> optAnthropometricData) {
        LOG.debug("Input parameters -> documentId {}, patientId {}, optAnthropometricData {}", documentId, patientId, optAnthropometricData);
        optAnthropometricData.ifPresent(anthropometricData -> {
            if(mustSaveClinicalObservation(anthropometricData.getHeight())) {
                ObservationVitalSign height = createObservationVitalSign(patientId, anthropometricData.getHeight(),
                        EVitalSign.HEIGHT);
                documentService.createDocumentVitalSign(documentId, height.getId());
                anthropometricData.setHeight(createObservationFromVitalSign(height));
            }

            if(mustSaveClinicalObservation(anthropometricData.getWeight())) {
                ObservationVitalSign weight = createObservationVitalSign(patientId, anthropometricData.getWeight(),
                        EVitalSign.WEIGHT);
                documentService.createDocumentVitalSign(documentId, weight.getId());
                anthropometricData.setWeight(createObservationFromVitalSign(weight));
            }

            if(mustSaveClinicalObservation(anthropometricData.getBloodType())) {
                ObservationLab bloodType = createObservationLab(patientId, anthropometricData.getBloodType(),
                        EObservationLab.BLOOD_TYPE);
                documentService.createDocumentLab(documentId, bloodType.getId());
                anthropometricData.setBloodType(createObservationFromLab(bloodType));
            }
        });
        LOG.debug(OUTPUT, optAnthropometricData);
        return optAnthropometricData.orElse(null);
    }

    private boolean mustSaveClinicalObservation(ClinicalObservationBo co) {
        return co != null && co.getValue() != null;
    }

    private ObservationVitalSign createObservationVitalSign(Integer patientId, ClinicalObservationBo observation, EVitalSign eVitalSign) {
        LOG.debug("Input parameters -> patientId {}, ClinicalObservation {}, eVitalSign {}", patientId, observation, eVitalSign);
        ObservationVitalSign observationVitalSign = observationVitalSignRepository.save(
                new ObservationVitalSign(patientId, observation.getValue(), eVitalSign, observation.getEffectiveTime()));
        LOG.debug(OUTPUT, observationVitalSign);
        return observationVitalSign;
    }

    private ObservationLab createObservationLab(Integer patientId, ClinicalObservationBo observation, EObservationLab eObservationLab) {
        LOG.debug("Input parameters -> patientId {}, ClinicalObservation {}, eLab {}", patientId, observation, eObservationLab);
        ObservationLab observationLab = observationLabRepository.save(
                new ObservationLab(patientId, observation.getValue(), eObservationLab, observation.getEffectiveTime()));
        LOG.debug(OUTPUT, observationLab);
        return observationLab;
    }


    private ClinicalObservationBo createObservationFromVitalSign(ObservationVitalSign vitalSign) {
        LOG.debug("Input parameters -> VitalSign {}", vitalSign);
        ClinicalObservationBo result = new ClinicalObservationBo(vitalSign.getId(), vitalSign.getValue(), vitalSign.getEffectiveTime());
        LOG.debug(OUTPUT, result);
        return result;
    }

    private ClinicalObservationBo createObservationFromLab(ObservationLab lab) {
        LOG.debug("Input parameters -> ObservationLab {}", lab);
        ClinicalObservationBo result = new ClinicalObservationBo(lab.getId(), lab.getValue(), lab.getEffectiveTime());
        LOG.debug(OUTPUT, result);
        return result;
    }

}
