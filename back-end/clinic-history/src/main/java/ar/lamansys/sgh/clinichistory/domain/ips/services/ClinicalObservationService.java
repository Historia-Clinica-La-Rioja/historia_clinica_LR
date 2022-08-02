package ar.lamansys.sgh.clinichistory.domain.ips.services;

import ar.lamansys.sgh.clinichistory.application.calculatecie10.CalculateCie10Facade;
import ar.lamansys.sgh.clinichistory.application.calculatecie10.Cie10FacadeRuleFeature;
import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.domain.ips.RiskFactorBo;
import ar.lamansys.sgh.clinichistory.domain.ips.RiskFactorObservationBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.ObservationLabRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.ObservationRiskFactorRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.ObservationLab;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.ObservationRiskFactor;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ClinicalObservationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.EObservationLab;
import ar.lamansys.sgh.clinichistory.domain.ips.ERiskFactor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class ClinicalObservationService {

    private static final Logger LOG = LoggerFactory.getLogger(ClinicalObservationService.class);

    public static final String OUTPUT = "Output -> {}";

    private final ObservationRiskFactorRepository observationRiskFactorRepository;

    private final ObservationLabRepository observationLabRepository;

    private final DocumentService documentService;

    private final SnomedService snomedService;

    private final CalculateCie10Facade calculateCie10Facade;

    public ClinicalObservationService(ObservationRiskFactorRepository observationRiskFactorRepository,
									  ObservationLabRepository observationLabRepository,
									  DocumentService documentService,
									  SnomedService snomedService,
									  CalculateCie10Facade calculateCie10Facade) {
        this.observationRiskFactorRepository = observationRiskFactorRepository;
        this.observationLabRepository = observationLabRepository;
        this.documentService = documentService;
        this.snomedService = snomedService;
        this.calculateCie10Facade = calculateCie10Facade;
    }

    public RiskFactorBo loadRiskFactors(PatientInfoBo patientInfo, Long documentId, Optional<RiskFactorBo> optRiskFactors) {
        LOG.debug("Input parameters -> documentId {}, patientInfo {}, optRiskFactors {}", documentId, patientInfo, optRiskFactors);
        optRiskFactors.ifPresent(RiskFactor -> {
            if(mustSaveClinicalObservation(RiskFactor.getSystolicBloodPressure())){
                ObservationRiskFactor systolicBloodPressure = createObservationRiskFactor(patientInfo,
                        RiskFactor.getSystolicBloodPressure(), ERiskFactor.SYSTOLIC_BLOOD_PRESSURE);
                if (documentId != null)
                    documentService.createDocumentRiskFactor(documentId, systolicBloodPressure.getId());
                RiskFactor.setSystolicBloodPressure(createObservationFromRiskFactor(systolicBloodPressure));
            }

            if(mustSaveClinicalObservation(RiskFactor.getDiastolicBloodPressure())) {
                ObservationRiskFactor diastolicBloodPressure = createObservationRiskFactor(patientInfo,
                        RiskFactor.getDiastolicBloodPressure(), ERiskFactor.DIASTOLIC_BLOOD_PRESSURE);
                if (documentId != null)
                    documentService.createDocumentRiskFactor(documentId, diastolicBloodPressure.getId());
                RiskFactor.setDiastolicBloodPressure(createObservationFromRiskFactor(diastolicBloodPressure));
            }

            if(mustSaveClinicalObservation(RiskFactor.getMeanPressure())) {
                ObservationRiskFactor meanPressure = createObservationRiskFactor(patientInfo,
                        RiskFactor.getMeanPressure(), ERiskFactor.MEAN_PRESSURE);
                if (documentId != null)
                    documentService.createDocumentRiskFactor(documentId, meanPressure.getId());
                RiskFactor.setMeanPressure(createObservationFromRiskFactor(meanPressure));
            }

            if(mustSaveClinicalObservation(RiskFactor.getTemperature())) {
                ObservationRiskFactor temperature = createObservationRiskFactor(patientInfo,
                        RiskFactor.getTemperature(), ERiskFactor.TEMPERATURE);
                if (documentId != null)
                    documentService.createDocumentRiskFactor(documentId, temperature.getId());
                RiskFactor.setTemperature(createObservationFromRiskFactor(temperature));
            }

            if(mustSaveClinicalObservation(RiskFactor.getHeartRate())) {
                ObservationRiskFactor heartRate = createObservationRiskFactor(patientInfo,
                        RiskFactor.getHeartRate(), ERiskFactor.HEART_RATE);
                if (documentId != null)
                    documentService.createDocumentRiskFactor(documentId, heartRate.getId());
                RiskFactor.setHeartRate(createObservationFromRiskFactor(heartRate));
            }

            if(mustSaveClinicalObservation(RiskFactor.getRespiratoryRate())) {
                ObservationRiskFactor respiratoryRate = createObservationRiskFactor(patientInfo,
                        RiskFactor.getRespiratoryRate(), ERiskFactor.RESPIRATORY_RATE);
                if (documentId != null)
                    documentService.createDocumentRiskFactor(documentId, respiratoryRate.getId());
                RiskFactor.setRespiratoryRate(createObservationFromRiskFactor(respiratoryRate));
            }

            if(mustSaveClinicalObservation(RiskFactor.getBloodOxygenSaturation())) {
                ObservationRiskFactor bloodOxygenSaturation = createObservationRiskFactor(patientInfo,
                        RiskFactor.getBloodOxygenSaturation(), ERiskFactor.BLOOD_OXYGEN_SATURATION);
                if (documentId != null)
                    documentService.createDocumentRiskFactor(documentId, bloodOxygenSaturation.getId());
                RiskFactor.setBloodOxygenSaturation(createObservationFromRiskFactor(bloodOxygenSaturation));
            }

            if(mustSaveClinicalObservation(RiskFactor.getBloodGlucose())) {
                ObservationRiskFactor bloodGlucose = createObservationRiskFactor(patientInfo,
                        RiskFactor.getBloodGlucose(), ERiskFactor.BLOOD_GLUCOSE);
                if (documentId != null)
                    documentService.createDocumentRiskFactor(documentId, bloodGlucose.getId());
                RiskFactor.setBloodGlucose(createObservationFromRiskFactor(bloodGlucose));
            }

            if(mustSaveClinicalObservation(RiskFactor.getGlycosylatedHemoglobin())) {
                ObservationRiskFactor glycosylatedHemoglobin = createObservationRiskFactor(patientInfo,
                        RiskFactor.getGlycosylatedHemoglobin(), ERiskFactor.GLYCOSYLATED_HEMOGLOBIN);
                if (documentId != null)
                    documentService.createDocumentRiskFactor(documentId, glycosylatedHemoglobin.getId());
                RiskFactor.setGlycosylatedHemoglobin(createObservationFromRiskFactor(glycosylatedHemoglobin));
            }

            if(mustSaveClinicalObservation(RiskFactor.getCardiovascularRisk())) {
                ObservationRiskFactor cardiovascularRisk = createObservationRiskFactor(patientInfo,
                        RiskFactor.getCardiovascularRisk(), ERiskFactor.CARDIOVASCULAR_RISK);
                if (documentId != null)
                    documentService.createDocumentRiskFactor(documentId, cardiovascularRisk.getId());
                RiskFactor.setCardiovascularRisk(createObservationFromRiskFactor(cardiovascularRisk));
            }
        });
        LOG.debug(OUTPUT, optRiskFactors);
        return optRiskFactors.orElse(null);
    }

    public AnthropometricDataBo loadAnthropometricData(PatientInfoBo patientInfo, Long documentId, Optional<AnthropometricDataBo> optAnthropometricData) {
        LOG.debug("Input parameters -> documentId {}, patientInfo {}, optAnthropometricData {}", documentId, patientInfo, optAnthropometricData);
        optAnthropometricData.ifPresent(anthropometricData -> {
			ClinicalObservationBo heightBo = anthropometricData.getHeight();
            if(mustSaveClinicalObservation(heightBo)) {
                ObservationRiskFactor height = mustSaveNewClinicalObservation(anthropometricData.getHeight())
						? createObservationRiskFactor(patientInfo, anthropometricData.getHeight(), ERiskFactor.HEIGHT)
						: mapToObservationRiskFactor(heightBo);
                documentService.createDocumentRiskFactor(documentId, height.getId());
                anthropometricData.setHeight(createObservationFromRiskFactor(height));
            }

			ClinicalObservationBo weightBo = anthropometricData.getWeight();
            if(mustSaveClinicalObservation(weightBo)) {
                ObservationRiskFactor weight = mustSaveNewClinicalObservation(weightBo)
						? createObservationRiskFactor(patientInfo, anthropometricData.getWeight(), ERiskFactor.WEIGHT)
						: mapToObservationRiskFactor(weightBo);
                documentService.createDocumentRiskFactor(documentId, weight.getId());
                anthropometricData.setWeight(createObservationFromRiskFactor(weight));
            }

			ClinicalObservationBo headCircumferenceBo = anthropometricData.getHeadCircumference();
			if(mustSaveClinicalObservation(headCircumferenceBo)) {
                ObservationRiskFactor headCircumference = mustSaveNewClinicalObservation(headCircumferenceBo)
						?createObservationRiskFactor(patientInfo, anthropometricData.getHeadCircumference(), ERiskFactor.HEAD_CIRCUMFERENCE)
						: mapToObservationRiskFactor(headCircumferenceBo);
                documentService.createDocumentRiskFactor(documentId, headCircumference.getId());
                anthropometricData.setHeadCircumference(createObservationFromRiskFactor(headCircumference));
            }

			ClinicalObservationBo bloodTypeBo = anthropometricData.getBloodType();
			if(mustSaveClinicalObservation(bloodTypeBo)) {
                ObservationLab bloodType =  createObservationLab(patientInfo, anthropometricData.getBloodType(), EObservationLab.BLOOD_TYPE);
                documentService.createDocumentLab(documentId, bloodType.getId());
                anthropometricData.setBloodType(createObservationFromLab(bloodType));
            }
        });
        LOG.debug(OUTPUT, optAnthropometricData);
        return optAnthropometricData.orElse(null);
    }

    public RiskFactorObservationBo getObservationById(Integer riskFactorObservationId) {
        LOG.debug("Input parameter -> riskFactorObservationId {}", riskFactorObservationId);
        return observationRiskFactorRepository.findById(riskFactorObservationId)
				.map(observationRiskFactor -> {
					RiskFactorObservationBo result = new RiskFactorObservationBo(observationRiskFactor);
					LOG.debug(OUTPUT, result);
					return result;
				}).get();
    }

    private boolean mustSaveClinicalObservation(ClinicalObservationBo co) {
        return co != null && co.getValue() != null;
    }

	private boolean mustSaveNewClinicalObservation(ClinicalObservationBo co) {
		return co != null && co.getValue() != null && co.getId()==null;
	}

    private ObservationRiskFactor createObservationRiskFactor(PatientInfoBo patientInfo, ClinicalObservationBo observation, ERiskFactor eRiskFactor) {
        LOG.debug("Input parameters -> patientInfo {}, ClinicalObservation {}, eRiskFactor {}", patientInfo, observation, eRiskFactor);
        Integer snomedId = snomedService.getLatestIdBySctid(eRiskFactor.getSctidCode())
                .orElseThrow(() -> new EntityNotFoundException("{snomed.not.found}"));
        String cie10Codes = calculateCie10Facade.execute(eRiskFactor.getSctidCode(),
                new Cie10FacadeRuleFeature(patientInfo.getGenderId(), patientInfo.getAge()));
        ObservationRiskFactor observationRiskFactor = observationRiskFactorRepository.save(
                new ObservationRiskFactor(patientInfo.getId(), observation.getValue(), snomedId, cie10Codes, eRiskFactor, observation.getEffectiveTime()));
        LOG.debug(OUTPUT, observationRiskFactor);
        return observationRiskFactor;
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

    private ClinicalObservationBo createObservationFromRiskFactor(ObservationRiskFactor riskFactor) {
        LOG.debug("Input parameters -> riskFactor {}", riskFactor);
        ClinicalObservationBo result = new ClinicalObservationBo(riskFactor.getId(), riskFactor.getValue(), riskFactor.getEffectiveTime());
        LOG.debug(OUTPUT, result);
        return result;
    }

    private ClinicalObservationBo createObservationFromLab(ObservationLab lab) {
        LOG.debug("Input parameters -> ObservationLab {}", lab);
        ClinicalObservationBo result = new ClinicalObservationBo(lab.getId(), lab.getValue(), lab.getEffectiveTime());
        LOG.debug(OUTPUT, result);
        return result;
    }

	private ObservationRiskFactor mapToObservationRiskFactor(ClinicalObservationBo bo) {
		LOG.debug("Input parameters -> ClinicalObservationBo {}", bo);
		ObservationRiskFactor result = new ObservationRiskFactor();
		result.setId(bo.getId());
		result.setValue(bo.getValue());
		result.setEffectiveTime(bo.getEffectiveTime());
		LOG.debug(OUTPUT, result);
		return result;
	}
	
}
