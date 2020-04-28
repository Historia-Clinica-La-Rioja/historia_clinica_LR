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

import java.util.List;
import java.util.stream.Collectors;

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
    public List<VitalSignBo> loadVitalSigns(Integer patientId, Long documentId, List<VitalSignBo> vitalSigns) {
        LOG.debug("Input parameters -> documentId {}, patientId {}, vitalSigns {}", documentId, patientId, vitalSigns);
        vitalSigns.stream().forEach(vitalSign -> {
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
        LOG.debug(OUTPUT, vitalSigns);
        return vitalSigns.stream().filter(v -> !v.wasDeleted()).collect(Collectors.toList());
    }

    @Override
    public List<AnthropometricDataBo> loadAnthropometricData(Integer patientId, Long documentId, List<AnthropometricDataBo> anthropometricDatas) {
        LOG.debug("Input parameters -> documentId {}, patientId {}, anthropometricData {}", documentId, patientId, anthropometricDatas);
        anthropometricDatas.forEach(anthropometricData -> {
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
                        ELab.BLOOD_TYPE);
                documentService.createDocumentLab(documentId, bloodType.getId());
                anthropometricData.setBloodType(createObservationFromLab(bloodType));
            }
        });
        LOG.debug(OUTPUT, anthropometricDatas);

        return anthropometricDatas.stream().filter(a -> !a.wasDeleted()).collect(Collectors.toList());
    }

    private boolean mustSaveClinicalObservation(ClinicalObservation co) {
        return co != null && co.mustSave();
    }

    private ObservationVitalSign createObservationVitalSign(Integer patientId, ClinicalObservation observation, EVitalSign eVitalSign) {
        LOG.debug("Input parameters -> patientId {}, ClinicalObservation {}, eVitalSign {}", patientId, observation, eVitalSign);
        ObservationVitalSign observationVitalSign = observationVitalSignRepository.save(new ObservationVitalSign(patientId, observation.getValue(), eVitalSign, observation.isDeleted()));
        LOG.debug(OUTPUT, observationVitalSign);
        return observationVitalSign;
    }

    private ObservationLab createObservationLab(Integer patientId, ClinicalObservation observation, ELab eLab) {
        LOG.debug("Input parameters -> patientId {}, ClinicalObservation {}, eLab {}, deleted {}", patientId, observation, eLab);
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
