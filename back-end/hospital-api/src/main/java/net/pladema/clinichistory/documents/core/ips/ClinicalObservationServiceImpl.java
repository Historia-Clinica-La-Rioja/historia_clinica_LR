package net.pladema.clinichistory.documents.core.ips;

import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.documents.repository.ips.ObservationLabRepository;
import net.pladema.clinichistory.documents.repository.ips.ObservationVitalSignRepository;
import net.pladema.clinichistory.documents.repository.ips.entity.ObservationLab;
import net.pladema.clinichistory.documents.repository.ips.entity.ObservationVitalSign;
import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.documents.service.ips.ClinicalObservationService;
import net.pladema.clinichistory.documents.service.ips.SnomedService;
import net.pladema.clinichistory.documents.service.ips.domain.AnthropometricDataBo;
import net.pladema.clinichistory.documents.service.ips.domain.ClinicalObservationBo;
import net.pladema.clinichistory.documents.service.ips.domain.VitalSignBo;
import net.pladema.clinichistory.documents.service.ips.domain.enums.EObservationLab;
import net.pladema.clinichistory.documents.service.ips.domain.enums.EVitalSign;
import net.pladema.patient.controller.dto.BasicPatientDto;
import net.pladema.snowstorm.services.CalculateCie10CodesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class ClinicalObservationServiceImpl implements ClinicalObservationService {

    private static final Logger LOG = LoggerFactory.getLogger(ClinicalObservationServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final ObservationVitalSignRepository observationVitalSignRepository;

    private final ObservationLabRepository observationLabRepository;

    private final DocumentService documentService;

    private final SnomedService snomedService;

    private final CalculateCie10CodesService calculateCie10CodesService;

    public ClinicalObservationServiceImpl(ObservationVitalSignRepository observationVitalSignRepository,
                                          ObservationLabRepository observationLabRepository,
                                          DocumentService documentService,
                                          SnomedService snomedService,
                                          CalculateCie10CodesService calculateCie10CodesService) {
        this.observationVitalSignRepository = observationVitalSignRepository;
        this.observationLabRepository = observationLabRepository;
        this.documentService = documentService;
        this.snomedService = snomedService;
        this.calculateCie10CodesService = calculateCie10CodesService;
    }

    @Override
    public VitalSignBo loadVitalSigns(PatientInfoBo patientInfo, Long documentId, Optional<VitalSignBo> optVitalSigns) {
        LOG.debug("Input parameters -> documentId {}, patientInfo {}, optVitalSigns {}", documentId, patientInfo, optVitalSigns);
        optVitalSigns.ifPresent(vitalSign -> {
            if(mustSaveClinicalObservation(vitalSign.getSystolicBloodPressure())){
                ObservationVitalSign systolicBloodPressure = createObservationVitalSign(patientInfo,
                        vitalSign.getSystolicBloodPressure(), EVitalSign.SYSTOLIC_BLOOD_PRESSURE);
                documentService.createDocumentVitalSign(documentId, systolicBloodPressure.getId());
                vitalSign.setSystolicBloodPressure(createObservationFromVitalSign(systolicBloodPressure));
            }

            if(mustSaveClinicalObservation(vitalSign.getDiastolicBloodPressure())) {
                ObservationVitalSign diastolicBloodPressure = createObservationVitalSign(patientInfo,
                        vitalSign.getDiastolicBloodPressure(), EVitalSign.DIASTOLIC_BLOOD_PRESSURE);
                documentService.createDocumentVitalSign(documentId, diastolicBloodPressure.getId());
                vitalSign.setDiastolicBloodPressure(createObservationFromVitalSign(diastolicBloodPressure));
            }

            if(mustSaveClinicalObservation(vitalSign.getMeanPressure())) {
                ObservationVitalSign meanPressure = createObservationVitalSign(patientInfo,
                        vitalSign.getMeanPressure(), EVitalSign.MEAN_PRESSURE);
                documentService.createDocumentVitalSign(documentId, meanPressure.getId());
                vitalSign.setMeanPressure(createObservationFromVitalSign(meanPressure));
            }

            if(mustSaveClinicalObservation(vitalSign.getTemperature())) {
                ObservationVitalSign temperature = createObservationVitalSign(patientInfo,
                        vitalSign.getTemperature(), EVitalSign.TEMPERATURE);
                documentService.createDocumentVitalSign(documentId, temperature.getId());
                vitalSign.setTemperature(createObservationFromVitalSign(temperature));
            }

            if(mustSaveClinicalObservation(vitalSign.getHeartRate())) {
                ObservationVitalSign heartRate = createObservationVitalSign(patientInfo,
                        vitalSign.getHeartRate(), EVitalSign.HEART_RATE);
                documentService.createDocumentVitalSign(documentId, heartRate.getId());
                vitalSign.setHeartRate(createObservationFromVitalSign(heartRate));
            }

            if(mustSaveClinicalObservation(vitalSign.getRespiratoryRate())) {
                ObservationVitalSign respiratoryRate = createObservationVitalSign(patientInfo,
                        vitalSign.getRespiratoryRate(), EVitalSign.RESPIRATORY_RATE);
                documentService.createDocumentVitalSign(documentId, respiratoryRate.getId());
                vitalSign.setRespiratoryRate(createObservationFromVitalSign(respiratoryRate));
            }

            if(mustSaveClinicalObservation(vitalSign.getBloodOxygenSaturation())) {
                ObservationVitalSign bloodOxygenSaturation = createObservationVitalSign(patientInfo,
                        vitalSign.getBloodOxygenSaturation(), EVitalSign.BLOOD_OXYGEN_SATURATION);
                documentService.createDocumentVitalSign(documentId, bloodOxygenSaturation.getId());
                vitalSign.setBloodOxygenSaturation(createObservationFromVitalSign(bloodOxygenSaturation));
            }
        });
        LOG.debug(OUTPUT, optVitalSigns);
        return optVitalSigns.orElse(null);
    }

    @Override
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

    private boolean mustSaveClinicalObservation(ClinicalObservationBo co) {
        return co != null && co.getValue() != null;
    }

    private ObservationVitalSign createObservationVitalSign(PatientInfoBo patientInfo, ClinicalObservationBo observation, EVitalSign eVitalSign) {
        LOG.debug("Input parameters -> patientInfo {}, ClinicalObservation {}, eVitalSign {}", patientInfo, observation, eVitalSign);
        Integer snomedId = snomedService.getLatestIdBySctid(eVitalSign.getSctidCode())
                .orElseThrow(() -> new EntityNotFoundException("{snomed.not.found}"));
        String cie10Codes = calculateCie10CodesService.execute(eVitalSign.getSctidCode(), patientInfo);
        ObservationVitalSign observationVitalSign = observationVitalSignRepository.save(
                new ObservationVitalSign(patientInfo.getId(), observation.getValue(), snomedId, cie10Codes, eVitalSign, observation.getEffectiveTime()));
        LOG.debug(OUTPUT, observationVitalSign);
        return observationVitalSign;
    }

    private ObservationLab createObservationLab(PatientInfoBo patientInfo, ClinicalObservationBo observation, EObservationLab eObservationLab) {
        LOG.debug("Input parameters -> patientInfo {}, ClinicalObservation {}, eLab {}", patientInfo, observation, eObservationLab);
        Integer snomedId = snomedService.getLatestIdBySctid(eObservationLab.getSctidCode())
                .orElseThrow(() -> new EntityNotFoundException("{snomed.not.found}"));
        String cie10Codes = calculateCie10CodesService.execute(eObservationLab.getSctidCode(), patientInfo);
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
