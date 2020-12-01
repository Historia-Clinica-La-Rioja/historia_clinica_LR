package net.pladema.clinichistory.documents.core.ips;

import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.documents.repository.ips.AllergyIntoleranceRepository;
import net.pladema.clinichistory.documents.repository.ips.entity.AllergyIntolerance;
import net.pladema.clinichistory.documents.repository.ips.masterdata.AllergyIntoleranceClinicalStatusRepository;
import net.pladema.clinichistory.documents.repository.ips.masterdata.AllergyIntoleranceVerificationStatusRepository;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.AllergyIntoleranceClinicalStatus;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.AllergyIntoleranceVerificationStatus;
import net.pladema.clinichistory.documents.service.ips.AllergyService;
import net.pladema.clinichistory.documents.service.ips.SnomedService;
import net.pladema.clinichistory.documents.service.ips.domain.AllergyConditionBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllergyServiceImpl implements AllergyService {

    private static final Logger LOG = LoggerFactory.getLogger(AllergyServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final AllergyIntoleranceRepository allergyIntoleranceRepository;

    private final AllergyIntoleranceClinicalStatusRepository allergyClinicalStatusRepository;

    private final AllergyIntoleranceVerificationStatusRepository allergyVerificationStatusRepository;

    private final DocumentService documentService;

    private final SnomedService snomedService;

    public AllergyServiceImpl(AllergyIntoleranceRepository allergyIntoleranceRepository,
                              AllergyIntoleranceClinicalStatusRepository allergyClinicalStatusRepository,
                              AllergyIntoleranceVerificationStatusRepository allergyVerificationStatusRepository,
                              DocumentService documentService,
                              SnomedService snomedService){
        this.allergyIntoleranceRepository = allergyIntoleranceRepository;
        this.allergyClinicalStatusRepository = allergyClinicalStatusRepository;
        this.allergyVerificationStatusRepository = allergyVerificationStatusRepository;
        this.documentService = documentService;
        this.snomedService = snomedService;
    }

    @Override
    public List<AllergyConditionBo> loadAllergies(Integer patientId, Long documentId, List<AllergyConditionBo> allergies) {
        LOG.debug("Input parameters -> patientId {}, documentId {}, allergies {}", documentId, patientId, allergies);
        allergies.forEach(allergy -> {
            String sctId = snomedService.createSnomedTerm(allergy.getSnomed());
            AllergyIntolerance allergyIntolerance = saveAllergyIntolerance(patientId, allergy, sctId);

            allergy.setId(allergyIntolerance.getId());
            allergy.setVerificationId(allergyIntolerance.getVerificationStatusId());
            allergy.setVerification(getVerification(allergy.getVerificationId()));
            allergy.setStatusId(allergyIntolerance.getStatusId());
            allergy.setStatus(getStatus(allergy.getStatusId()));
            allergy.setCategoryId(allergyIntolerance.getCategoryId());
            allergy.setDate(allergyIntolerance.getStartDate());

            documentService.createDocumentAllergyIntolerance(documentId, allergyIntolerance.getId());
        });
        List<AllergyConditionBo> result = allergies;
        LOG.debug(OUTPUT, result);
        return result;
    }

    private AllergyIntolerance saveAllergyIntolerance(Integer patientId, AllergyConditionBo allergy, String sctId) {
        LOG.debug("Input parameters -> patientId {}, allergy {}, sctId {}", patientId, allergy, sctId);
        AllergyIntolerance allergyIntolerance = new AllergyIntolerance(patientId,
                sctId,
                allergy.getStatusId(),
                allergy.getVerificationId(),
                allergy.getCategoryId(),
                allergy.getDate());
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
