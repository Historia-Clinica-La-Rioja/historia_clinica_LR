package ar.lamansys.sgh.clinichistory.domain.ips.services;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.domain.ips.RiskFactorBo;
import ar.lamansys.sgh.clinichistory.domain.ips.RiskFactorObservationBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.ObservationLabRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.ObservationRiskFactorRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.ObservationLab;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.ObservationRiskFactor;
import ar.lamansys.sgh.clinichistory.domain.ips.AnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ClinicalObservationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EObservationLab;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.ERiskFactor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ClinicalObservationService {

    public static final String OUTPUT = "Output -> {}";

    private final ObservationRiskFactorRepository observationRiskFactorRepository;

    private final ObservationLabRepository observationLabRepository;

    private final DocumentService documentService;

    private final SnomedService snomedService;

    public RiskFactorBo loadRiskFactors(Integer patientId, Long documentId, Optional<RiskFactorBo> optRiskFactors) {
        log.debug("Input parameters -> documentId {}, patientId {}, optRiskFactors {}", documentId, patientId, optRiskFactors);
        optRiskFactors.ifPresent(RiskFactor -> {
            if (mustSaveClinicalObservation(RiskFactor.getSystolicBloodPressure())) {
                if (RiskFactor.getSystolicBloodPressure().getId() == null) {
                    ObservationRiskFactor systolicBloodPressure = createObservationRiskFactor(patientId,
                            RiskFactor.getSystolicBloodPressure(), ERiskFactor.SYSTOLIC_BLOOD_PRESSURE);
                    if (documentId != null)
                        documentService.createDocumentRiskFactor(documentId, systolicBloodPressure.getId());
                    RiskFactor.setSystolicBloodPressure(createObservationFromRiskFactor(systolicBloodPressure));
                } else
                    documentService.createDocumentRiskFactor(documentId, RiskFactor.getSystolicBloodPressure().getId());
            }

            if (mustSaveClinicalObservation(RiskFactor.getDiastolicBloodPressure())) {
                if (RiskFactor.getDiastolicBloodPressure().getId() == null) {
                    ObservationRiskFactor diastolicBloodPressure = createObservationRiskFactor(patientId,
                            RiskFactor.getDiastolicBloodPressure(), ERiskFactor.DIASTOLIC_BLOOD_PRESSURE);
                    if (documentId != null)
                        documentService.createDocumentRiskFactor(documentId, diastolicBloodPressure.getId());
                    RiskFactor.setDiastolicBloodPressure(createObservationFromRiskFactor(diastolicBloodPressure));
                } else
                    documentService.createDocumentRiskFactor(documentId, RiskFactor.getDiastolicBloodPressure().getId());
            }

            if (mustSaveClinicalObservation(RiskFactor.getMeanPressure())) {
                if (RiskFactor.getMeanPressure().getId() == null) {
                    ObservationRiskFactor meanPressure = createObservationRiskFactor(patientId,
                            RiskFactor.getMeanPressure(), ERiskFactor.MEAN_PRESSURE);
                    if (documentId != null)
                        documentService.createDocumentRiskFactor(documentId, meanPressure.getId());
                    RiskFactor.setMeanPressure(createObservationFromRiskFactor(meanPressure));
                } else
                    documentService.createDocumentRiskFactor(documentId, RiskFactor.getMeanPressure().getId());
            }

            if (mustSaveClinicalObservation(RiskFactor.getTemperature())) {
                if (RiskFactor.getTemperature().getId() == null) {
                    ObservationRiskFactor temperature = createObservationRiskFactor(patientId,
                            RiskFactor.getTemperature(), ERiskFactor.TEMPERATURE);
                    if (documentId != null)
                        documentService.createDocumentRiskFactor(documentId, temperature.getId());
                    RiskFactor.setTemperature(createObservationFromRiskFactor(temperature));
                } else
                    documentService.createDocumentRiskFactor(documentId, RiskFactor.getTemperature().getId());
            }

            if (mustSaveClinicalObservation(RiskFactor.getHeartRate())) {
                if (RiskFactor.getHeartRate().getId() == null) {
                    ObservationRiskFactor heartRate = createObservationRiskFactor(patientId,
                            RiskFactor.getHeartRate(), ERiskFactor.HEART_RATE);
                    if (documentId != null)
                        documentService.createDocumentRiskFactor(documentId, heartRate.getId());
                    RiskFactor.setHeartRate(createObservationFromRiskFactor(heartRate));
                } else
                    documentService.createDocumentRiskFactor(documentId, RiskFactor.getHeartRate().getId());
            }

            if (mustSaveClinicalObservation(RiskFactor.getRespiratoryRate())) {
                if (RiskFactor.getRespiratoryRate().getId() == null) {
                    ObservationRiskFactor respiratoryRate = createObservationRiskFactor(patientId,
                            RiskFactor.getRespiratoryRate(), ERiskFactor.RESPIRATORY_RATE);
                    if (documentId != null)
                        documentService.createDocumentRiskFactor(documentId, respiratoryRate.getId());
                    RiskFactor.setRespiratoryRate(createObservationFromRiskFactor(respiratoryRate));
                } else
                    documentService.createDocumentRiskFactor(documentId, RiskFactor.getRespiratoryRate().getId());
            }

            if (mustSaveClinicalObservation(RiskFactor.getBloodOxygenSaturation())) {
                if (RiskFactor.getBloodOxygenSaturation().getId() == null) {
                    ObservationRiskFactor bloodOxygenSaturation = createObservationRiskFactor(patientId,
                            RiskFactor.getBloodOxygenSaturation(), ERiskFactor.BLOOD_OXYGEN_SATURATION);
                    if (documentId != null)
                        documentService.createDocumentRiskFactor(documentId, bloodOxygenSaturation.getId());
                    RiskFactor.setBloodOxygenSaturation(createObservationFromRiskFactor(bloodOxygenSaturation));
                } else
                    documentService.createDocumentRiskFactor(documentId, RiskFactor.getBloodOxygenSaturation().getId());
            }

            if (mustSaveClinicalObservation(RiskFactor.getBloodGlucose())) {
                if (RiskFactor.getBloodGlucose().getId() == null) {
                    ObservationRiskFactor bloodGlucose = createObservationRiskFactor(patientId,
                            RiskFactor.getBloodGlucose(), ERiskFactor.BLOOD_GLUCOSE);
                    if (documentId != null)
                        documentService.createDocumentRiskFactor(documentId, bloodGlucose.getId());
                    RiskFactor.setBloodGlucose(createObservationFromRiskFactor(bloodGlucose));
                } else
                    documentService.createDocumentRiskFactor(documentId, RiskFactor.getBloodGlucose().getId());
            }

            if (mustSaveClinicalObservation(RiskFactor.getGlycosylatedHemoglobin())) {
                if (RiskFactor.getGlycosylatedHemoglobin().getId() == null) {
                    ObservationRiskFactor glycosylatedHemoglobin = createObservationRiskFactor(patientId,
                            RiskFactor.getGlycosylatedHemoglobin(), ERiskFactor.GLYCOSYLATED_HEMOGLOBIN);
                    if (documentId != null)
                        documentService.createDocumentRiskFactor(documentId, glycosylatedHemoglobin.getId());
                    RiskFactor.setGlycosylatedHemoglobin(createObservationFromRiskFactor(glycosylatedHemoglobin));
                } else
                    documentService.createDocumentRiskFactor(documentId, RiskFactor.getGlycosylatedHemoglobin().getId());
            }

            if (mustSaveClinicalObservation(RiskFactor.getCardiovascularRisk())) {
                if (RiskFactor.getCardiovascularRisk().getId() == null) {
                    ObservationRiskFactor cardiovascularRisk = createObservationRiskFactor(patientId,
                            RiskFactor.getCardiovascularRisk(), ERiskFactor.CARDIOVASCULAR_RISK);
                    if (documentId != null)
                        documentService.createDocumentRiskFactor(documentId, cardiovascularRisk.getId());
                    RiskFactor.setCardiovascularRisk(createObservationFromRiskFactor(cardiovascularRisk));
                } else
                    documentService.createDocumentRiskFactor(documentId, RiskFactor.getCardiovascularRisk().getId());
            }
            if (mustSaveClinicalObservation(RiskFactor.getHematocrit())) {
                if (RiskFactor.getHematocrit().getId() == null) {
                    ObservationRiskFactor hematocrit = createObservationRiskFactor(patientId,
                            RiskFactor.getHematocrit(), ERiskFactor.HEMATOCRIT);
                    if (documentId != null)
                        documentService.createDocumentRiskFactor(documentId, hematocrit.getId());
                    RiskFactor.setHematocrit(createObservationFromRiskFactor(hematocrit));
                } else
                    documentService.createDocumentRiskFactor(documentId, RiskFactor.getHematocrit().getId());
            }
        });
        log.debug(OUTPUT, optRiskFactors);
        return optRiskFactors.orElse(null);
    }

    public AnthropometricDataBo loadAnthropometricData(Integer patientId, Long documentId, Optional<AnthropometricDataBo> optAnthropometricData) {
        log.debug("Input parameters -> documentId {}, patientId {}, optAnthropometricData {}", documentId, patientId, optAnthropometricData);
        optAnthropometricData.ifPresent(anthropometricData -> {
            ClinicalObservationBo heightBo = anthropometricData.getHeight();
            if (mustSaveClinicalObservation(heightBo)) {
                ObservationRiskFactor height = mustSaveNewClinicalObservation(anthropometricData.getHeight())
                        ? createObservationRiskFactor(patientId, anthropometricData.getHeight(), ERiskFactor.HEIGHT)
                        : mapToObservationRiskFactor(heightBo);
                documentService.createDocumentRiskFactor(documentId, height.getId());
                anthropometricData.setHeight(createObservationFromRiskFactor(height));
            }

            ClinicalObservationBo weightBo = anthropometricData.getWeight();
            if (mustSaveClinicalObservation(weightBo)) {
                ObservationRiskFactor weight = mustSaveNewClinicalObservation(weightBo)
                        ? createObservationRiskFactor(patientId, anthropometricData.getWeight(), ERiskFactor.WEIGHT)
                        : mapToObservationRiskFactor(weightBo);
                documentService.createDocumentRiskFactor(documentId, weight.getId());
                anthropometricData.setWeight(createObservationFromRiskFactor(weight));
            }

            ClinicalObservationBo headCircumferenceBo = anthropometricData.getHeadCircumference();
            if (mustSaveClinicalObservation(headCircumferenceBo)) {
                ObservationRiskFactor headCircumference = mustSaveNewClinicalObservation(headCircumferenceBo)
                        ? createObservationRiskFactor(patientId, anthropometricData.getHeadCircumference(), ERiskFactor.HEAD_CIRCUMFERENCE)
                        : mapToObservationRiskFactor(headCircumferenceBo);
                documentService.createDocumentRiskFactor(documentId, headCircumference.getId());
                anthropometricData.setHeadCircumference(createObservationFromRiskFactor(headCircumference));
            }

            ClinicalObservationBo bloodTypeBo = anthropometricData.getBloodType();
            if (mustSaveClinicalObservation(bloodTypeBo)) {
                ObservationLab bloodType = createObservationLab(patientId, anthropometricData.getBloodType(), EObservationLab.BLOOD_TYPE);
                documentService.createDocumentLab(documentId, bloodType.getId());
                anthropometricData.setBloodType(createObservationFromLab(bloodType));
            }
        });
        log.debug(OUTPUT, optAnthropometricData);
        return optAnthropometricData.orElse(null);
    }

    public RiskFactorObservationBo getObservationById(Integer riskFactorObservationId) {
        log.debug("Input parameter -> riskFactorObservationId {}", riskFactorObservationId);
        return observationRiskFactorRepository.findById(riskFactorObservationId)
                .map(observationRiskFactor -> {
                    RiskFactorObservationBo result = new RiskFactorObservationBo(observationRiskFactor);
                    log.debug(OUTPUT, result);
                    return result;
                }).get();
    }

    private boolean mustSaveClinicalObservation(ClinicalObservationBo co) {
        return co != null && co.getValue() != null;
    }

    private boolean mustSaveNewClinicalObservation(ClinicalObservationBo co) {
        return co != null && co.getValue() != null && co.getId() == null;
    }

    private ObservationRiskFactor createObservationRiskFactor(Integer patientId, ClinicalObservationBo observation, ERiskFactor eRiskFactor) {
        log.debug("Input parameters -> patientId {}, ClinicalObservation {}, eRiskFactor {}", patientId, observation, eRiskFactor);
        Integer snomedId = snomedService.getLatestIdBySctid(eRiskFactor.getSctidCode())
                .orElseThrow(() -> new EntityNotFoundException("{snomed.not.found}"));
        ObservationRiskFactor observationRiskFactor = observationRiskFactorRepository.save(
                new ObservationRiskFactor(patientId, observation.getValue(), snomedId, eRiskFactor, observation.getEffectiveTime(), observation.getStatusId()));
        log.debug(OUTPUT, observationRiskFactor);
        return observationRiskFactor;
    }

    private ObservationLab createObservationLab(Integer patientId, ClinicalObservationBo observation, EObservationLab eObservationLab) {
        log.debug("Input parameters -> patientId {}, ClinicalObservation {}, eLab {}", patientId, observation, eObservationLab);
        Integer snomedId = snomedService.getLatestIdBySctid(eObservationLab.getSctidCode())
                .orElseThrow(() -> new EntityNotFoundException("{snomed.not.found}"));
        ObservationLab observationLab = observationLabRepository.save(
                new ObservationLab(patientId, observation.getValue(), snomedId, observation.getEffectiveTime(), observation.getStatusId()));
        log.debug(OUTPUT, observationLab);
        return observationLab;
    }

    private ClinicalObservationBo createObservationFromRiskFactor(ObservationRiskFactor riskFactor) {
        log.debug("Input parameters -> riskFactor {}", riskFactor);
        ClinicalObservationBo result = new ClinicalObservationBo(riskFactor.getId(), riskFactor.getValue(), riskFactor.getEffectiveTime(), riskFactor.getStatusId());
        log.debug(OUTPUT, result);
        return result;
    }

    private ClinicalObservationBo createObservationFromLab(ObservationLab lab) {
        log.debug("Input parameters -> ObservationLab {}", lab);
        ClinicalObservationBo result = new ClinicalObservationBo(lab.getId(), lab.getValue(), lab.getEffectiveTime(), lab.getStatusId());
        log.debug(OUTPUT, result);
        return result;
    }

    private ObservationRiskFactor mapToObservationRiskFactor(ClinicalObservationBo bo) {
        log.debug("Input parameters -> ClinicalObservationBo {}", bo);
        ObservationRiskFactor result = new ObservationRiskFactor();
        result.setId(bo.getId());
        result.setValue(bo.getValue());
        result.setEffectiveTime(bo.getEffectiveTime());
        result.setStatusId(bo.getStatusId());
        log.debug(OUTPUT, result);
        return result;
    }

}
