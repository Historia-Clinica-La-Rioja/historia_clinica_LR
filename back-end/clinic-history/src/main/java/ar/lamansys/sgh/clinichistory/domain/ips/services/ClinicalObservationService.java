package ar.lamansys.sgh.clinichistory.domain.ips.services;

import ar.lamansys.sgh.clinichistory.application.calculatecie10.CalculateCie10Facade;
import ar.lamansys.sgh.clinichistory.application.calculatecie10.Cie10FacadeRuleFeature;
import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.ObservationLabRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.ObservationVitalSignRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.ObservationLab;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.ObservationVitalSign;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ClinicalObservationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.VitalSignBo;
import ar.lamansys.sgh.clinichistory.domain.ips.VitalSignObservationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.EObservationLab;
import ar.lamansys.sgh.clinichistory.domain.ips.EVitalSign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class ClinicalObservationService {

    private static final Logger LOG = LoggerFactory.getLogger(ClinicalObservationService.class);

    public static final String OUTPUT = "Output -> {}";

    private final ObservationVitalSignRepository observationVitalSignRepository;

    private final ObservationLabRepository observationLabRepository;

    private final DocumentService documentService;

    private final SnomedService snomedService;

    private final CalculateCie10Facade calculateCie10Facade;

    public ClinicalObservationService(ObservationVitalSignRepository observationVitalSignRepository,
                                      ObservationLabRepository observationLabRepository,
                                      DocumentService documentService,
                                      SnomedService snomedService,
                                      CalculateCie10Facade calculateCie10Facade) {
        this.observationVitalSignRepository = observationVitalSignRepository;
        this.observationLabRepository = observationLabRepository;
        this.documentService = documentService;
        this.snomedService = snomedService;
        this.calculateCie10Facade = calculateCie10Facade;
    }

    public VitalSignBo loadVitalSigns(PatientInfoBo patientInfo, Long documentId, Optional<VitalSignBo> optVitalSigns) {
        LOG.debug("Input parameters -> documentId {}, patientInfo {}, optVitalSigns {}", documentId, patientInfo, optVitalSigns);
        optVitalSigns.ifPresent(vitalSign -> {
            if(mustSaveClinicalObservation(vitalSign.getSystolicBloodPressure())){
                ObservationVitalSign systolicBloodPressure = createObservationVitalSign(patientInfo,
                        vitalSign.getSystolicBloodPressure(), EVitalSign.SYSTOLIC_BLOOD_PRESSURE);
                if (documentId != null)
                    documentService.createDocumentVitalSign(documentId, systolicBloodPressure.getId());
                vitalSign.setSystolicBloodPressure(createObservationFromVitalSign(systolicBloodPressure));
            }

            if(mustSaveClinicalObservation(vitalSign.getDiastolicBloodPressure())) {
                ObservationVitalSign diastolicBloodPressure = createObservationVitalSign(patientInfo,
                        vitalSign.getDiastolicBloodPressure(), EVitalSign.DIASTOLIC_BLOOD_PRESSURE);
                if (documentId != null)
                    documentService.createDocumentVitalSign(documentId, diastolicBloodPressure.getId());
                vitalSign.setDiastolicBloodPressure(createObservationFromVitalSign(diastolicBloodPressure));
            }

            if(mustSaveClinicalObservation(vitalSign.getMeanPressure())) {
                ObservationVitalSign meanPressure = createObservationVitalSign(patientInfo,
                        vitalSign.getMeanPressure(), EVitalSign.MEAN_PRESSURE);
                if (documentId != null)
                    documentService.createDocumentVitalSign(documentId, meanPressure.getId());
                vitalSign.setMeanPressure(createObservationFromVitalSign(meanPressure));
            }

            if(mustSaveClinicalObservation(vitalSign.getTemperature())) {
                ObservationVitalSign temperature = createObservationVitalSign(patientInfo,
                        vitalSign.getTemperature(), EVitalSign.TEMPERATURE);
                if (documentId != null)
                    documentService.createDocumentVitalSign(documentId, temperature.getId());
                vitalSign.setTemperature(createObservationFromVitalSign(temperature));
            }

            if(mustSaveClinicalObservation(vitalSign.getHeartRate())) {
                ObservationVitalSign heartRate = createObservationVitalSign(patientInfo,
                        vitalSign.getHeartRate(), EVitalSign.HEART_RATE);
                if (documentId != null)
                    documentService.createDocumentVitalSign(documentId, heartRate.getId());
                vitalSign.setHeartRate(createObservationFromVitalSign(heartRate));
            }

            if(mustSaveClinicalObservation(vitalSign.getRespiratoryRate())) {
                ObservationVitalSign respiratoryRate = createObservationVitalSign(patientInfo,
                        vitalSign.getRespiratoryRate(), EVitalSign.RESPIRATORY_RATE);
                if (documentId != null)
                    documentService.createDocumentVitalSign(documentId, respiratoryRate.getId());
                vitalSign.setRespiratoryRate(createObservationFromVitalSign(respiratoryRate));
            }

            if(mustSaveClinicalObservation(vitalSign.getBloodOxygenSaturation())) {
                ObservationVitalSign bloodOxygenSaturation = createObservationVitalSign(patientInfo,
                        vitalSign.getBloodOxygenSaturation(), EVitalSign.BLOOD_OXYGEN_SATURATION);
                if (documentId != null)
                    documentService.createDocumentVitalSign(documentId, bloodOxygenSaturation.getId());
                vitalSign.setBloodOxygenSaturation(createObservationFromVitalSign(bloodOxygenSaturation));
            }

            if(mustSaveClinicalObservation(vitalSign.getBloodGlucose())) {
                ObservationVitalSign bloodGlucose = createObservationVitalSign(patientInfo,
                        vitalSign.getBloodGlucose(), EVitalSign.BLOOD_GLUCOSE);
                if (documentId != null)
                    documentService.createDocumentVitalSign(documentId, bloodGlucose.getId());
                vitalSign.setBloodGlucose(createObservationFromVitalSign(bloodGlucose));
            }

            if(mustSaveClinicalObservation(vitalSign.getGlycosylatedHemoglobin())) {
                ObservationVitalSign glycosylatedHemoglobin = createObservationVitalSign(patientInfo,
                        vitalSign.getGlycosylatedHemoglobin(), EVitalSign.GLYCOSYLATED_HEMOGLOBIN);
                if (documentId != null)
                    documentService.createDocumentVitalSign(documentId, glycosylatedHemoglobin.getId());
                vitalSign.setGlycosylatedHemoglobin(createObservationFromVitalSign(glycosylatedHemoglobin));
            }

            if(mustSaveClinicalObservation(vitalSign.getCardiovascularRisk())) {
                ObservationVitalSign cardiovascularRisk = createObservationVitalSign(patientInfo,
                        vitalSign.getCardiovascularRisk(), EVitalSign.CARDIOVASCULAR_RISK);
                if (documentId != null)
                    documentService.createDocumentVitalSign(documentId, cardiovascularRisk.getId());
                vitalSign.setCardiovascularRisk(createObservationFromVitalSign(cardiovascularRisk));
            }
        });
        LOG.debug(OUTPUT, optVitalSigns);
        return optVitalSigns.orElse(null);
    }

    public AnthropometricDataBo loadAnthropometricData(PatientInfoBo patientInfo, Long documentId, Optional<AnthropometricDataBo> optAnthropometricData) {
        LOG.debug("Input parameters -> documentId {}, patientInfo {}, optAnthropometricData {}", documentId, patientInfo, optAnthropometricData);
        optAnthropometricData.ifPresent(anthropometricData -> {
            if(mustSaveClinicalObservation(anthropometricData.getHeight())) {
                ObservationVitalSign height = createObservationVitalSign(patientInfo, anthropometricData.getHeight(),
                        EVitalSign.HEIGHT);
                documentService.createDocumentVitalSign(documentId, height.getId());
                anthropometricData.setHeight(createObservationFromVitalSign(height));
            }

            if(mustSaveClinicalObservation(anthropometricData.getWeight())) {
                ObservationVitalSign weight = createObservationVitalSign(patientInfo, anthropometricData.getWeight(),
                        EVitalSign.WEIGHT);
                documentService.createDocumentVitalSign(documentId, weight.getId());
                anthropometricData.setWeight(createObservationFromVitalSign(weight));
            }

            if(mustSaveClinicalObservation(anthropometricData.getHeadCircumference())) {
                ObservationVitalSign headCircumference = createObservationVitalSign(patientInfo, anthropometricData.getHeadCircumference(),
                        EVitalSign.HEAD_CIRCUMFERENCE);
                documentService.createDocumentVitalSign(documentId, headCircumference.getId());
                anthropometricData.setHeadCircumference(createObservationFromVitalSign(headCircumference));
            }

            if(mustSaveClinicalObservation(anthropometricData.getBloodType())) {
                ObservationLab bloodType = createObservationLab(patientInfo, anthropometricData.getBloodType(),
                        EObservationLab.BLOOD_TYPE);
                documentService.createDocumentLab(documentId, bloodType.getId());
                anthropometricData.setBloodType(createObservationFromLab(bloodType));
            }
        });
        LOG.debug(OUTPUT, optAnthropometricData);
        return optAnthropometricData.orElse(null);
    }

    public VitalSignObservationBo getObservationById(Integer vitalSignObservationId) {
        LOG.debug("Input parameter -> vitalSignObservationId {}", vitalSignObservationId);
        ObservationVitalSign observationVitalSign = observationVitalSignRepository.getOne(vitalSignObservationId);
        VitalSignObservationBo result = new VitalSignObservationBo(observationVitalSign);
        LOG.debug(OUTPUT, result);
        return result;
    }

    private boolean mustSaveClinicalObservation(ClinicalObservationBo co) {
        return co != null && co.getValue() != null;
    }

    private ObservationVitalSign createObservationVitalSign(PatientInfoBo patientInfo, ClinicalObservationBo observation, EVitalSign eVitalSign) {
        LOG.debug("Input parameters -> patientInfo {}, ClinicalObservation {}, eVitalSign {}", patientInfo, observation, eVitalSign);
        Integer snomedId = snomedService.getLatestIdBySctid(eVitalSign.getSctidCode())
                .orElseThrow(() -> new EntityNotFoundException("{snomed.not.found}"));
        String cie10Codes = calculateCie10Facade.execute(eVitalSign.getSctidCode(),
                new Cie10FacadeRuleFeature(patientInfo.getGenderId(), patientInfo.getAge()));
        ObservationVitalSign observationVitalSign = observationVitalSignRepository.save(
                new ObservationVitalSign(patientInfo.getId(), observation.getValue(), snomedId, cie10Codes, eVitalSign, observation.getEffectiveTime()));
        LOG.debug(OUTPUT, observationVitalSign);
        return observationVitalSign;
    }

    private ObservationLab createObservationLab(PatientInfoBo patientInfo, ClinicalObservationBo observation, EObservationLab eObservationLab) {
        LOG.debug("Input parameters -> patientInfo {}, ClinicalObservation {}, eLab {}", patientInfo, observation, eObservationLab);
        Integer snomedId = snomedService.getLatestIdBySctid(eObservationLab.getSctidCode())
                .orElseThrow(() -> new EntityNotFoundException("{snomed.not.found}"));
        String cie10Codes = calculateCie10Facade.execute(eObservationLab.getSctidCode(),
                new Cie10FacadeRuleFeature(patientInfo.getGenderId(), patientInfo.getAge()));
        ObservationLab observationLab = observationLabRepository.save(
                new ObservationLab(patientInfo.getId(), observation.getValue(), snomedId, cie10Codes, observation.getEffectiveTime()));
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
