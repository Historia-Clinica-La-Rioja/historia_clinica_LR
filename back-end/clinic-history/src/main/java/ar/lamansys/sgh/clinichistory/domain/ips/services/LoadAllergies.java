package ar.lamansys.sgh.clinichistory.domain.ips.services;

import ar.lamansys.sgh.clinichistory.application.calculatecie10.CalculateCie10Facade;
import ar.lamansys.sgh.clinichistory.application.calculatecie10.Cie10FacadeRuleFeature;
import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.domain.ReferableItemBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.AllergyIntoleranceRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.AllergyIntolerance;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.AllergyIntoleranceClinicalStatusRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.AllergyIntoleranceVerificationStatusRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.AllergyIntoleranceClinicalStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.AllergyIntoleranceVerificationStatus;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AllergyConditionBo;
import lombok.RequiredArgsConstructor;
import net.pladema.snowstorm.services.SnowstormInferredService;
import net.pladema.snowstorm.services.inferredrules.InferredAllergyAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class LoadAllergies {

    private static final Logger LOG = LoggerFactory.getLogger(LoadAllergies.class);

    public static final String OUTPUT = "Output -> {}";

    private final AllergyIntoleranceRepository allergyIntoleranceRepository;

    private final AllergyIntoleranceClinicalStatusRepository allergyClinicalStatusRepository;

    private final AllergyIntoleranceVerificationStatusRepository allergyVerificationStatusRepository;

    private final DocumentService documentService;

    private final SnomedService snomedService;

    private final CalculateCie10Facade calculateCie10Facade;

    private final SnowstormInferredService snowstormInferredService;

    public List<AllergyConditionBo> run(PatientInfoBo patientInfo, Long documentId, ReferableItemBo<AllergyConditionBo> allergies) {
        LOG.debug("Input parameters -> patientInfo {}, documentId {}, allergies {}", patientInfo, documentId, allergies);
		if (allergies != null)
			return getAllergyCondition(patientInfo, documentId, allergies);
		return new ArrayList<>();
    }

	private @Nullable List<AllergyConditionBo> getAllergyCondition(PatientInfoBo patientInfo, Long documentId, ReferableItemBo<AllergyConditionBo> allergies) {
		documentService.createDocumentRefersAllergy(documentId, allergies.getIsReferred());
		List<AllergyConditionBo> result = saveAllergies(patientInfo, documentId, allergies.getContent());
		LOG.debug(OUTPUT, result);
		return result;
	}

	private List<AllergyConditionBo> saveAllergies(PatientInfoBo patientInfo, Long documentId, List<AllergyConditionBo> allergies) {
		if (allergies != null)
			allergies.forEach(allergy -> processAllergy(patientInfo, documentId, allergy));
		return allergies;
	}

	private void processAllergy(PatientInfoBo patientInfo, Long documentId, AllergyConditionBo allergy) {
		if(allergy.getId() == null)
			initializeAllergy(patientInfo, allergy);
		documentService.createDocumentAllergyIntolerance(documentId, allergy.getId());
	}

	private void initializeAllergy(PatientInfoBo patientInfo, AllergyConditionBo allergy) {
		Integer snomedId = snomedService.getSnomedId(allergy.getSnomed()).orElseGet(() -> snomedService.createSnomedTerm(allergy.getSnomed()));
		String cie10Codes = calculateCie10Facade.execute(allergy.getSnomed().getSctid(), new Cie10FacadeRuleFeature(patientInfo.getGenderId(), patientInfo.getAge()));
		AllergyIntolerance allergyIntolerance = saveAllergyIntolerance(patientInfo, allergy, snomedId, cie10Codes);

		allergy.setId(allergyIntolerance.getId());
		allergy.setVerificationId(allergyIntolerance.getVerificationStatusId());
		allergy.setVerification(getVerification(allergy.getVerificationId()));
		allergy.setStatusId(allergyIntolerance.getStatusId());
		allergy.setStatus(getStatus(allergy.getStatusId()));
		allergy.setCategoryId(allergyIntolerance.getCategoryId());
		allergy.setDate(allergyIntolerance.getStartDate());
	}

	private AllergyIntolerance saveAllergyIntolerance(PatientInfoBo patientInfo, AllergyConditionBo allergy, Integer snomedId, String cie10Codes) {
        LOG.debug("Input parameters -> patientInfo {}, allergy {}, snomedId {}", patientInfo, allergy, snomedId);
        AllergyIntolerance allergyIntolerance = new AllergyIntolerance(patientInfo.getId(),
                snomedId,
                cie10Codes,
                allergy.getStatusId(),
                allergy.getVerificationId(),
                allergy.getCategoryId(),
                allergy.getCriticalityId(),
                allergy.getDate());

        InferredAllergyAttributes inferredAttributes = snowstormInferredService.
                getInferredAllergyAttributes(allergy.getSnomed().getSctid());
        allergyIntolerance.setCategoryId(inferredAttributes.getCategory());
        allergyIntolerance.setType(inferredAttributes.getType());

        allergyIntolerance = allergyIntoleranceRepository.save(allergyIntolerance);
        LOG.debug("allergyIntolerance saved -> {} ", allergyIntolerance.getId());
        LOG.debug(OUTPUT, allergyIntolerance);
        return allergyIntolerance;
    }

    private String getVerification(String id) {
        return allergyVerificationStatusRepository.findById(id).map(AllergyIntoleranceVerificationStatus::getDescription).orElse(null);
    }

    private String getStatus(String id) {
        return allergyClinicalStatusRepository.findById(id).map(AllergyIntoleranceClinicalStatus::getDescription).orElse(null);
    }

}
