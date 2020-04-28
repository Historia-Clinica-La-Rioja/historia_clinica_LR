package net.pladema.internation.service.documents.anamnesis.impl;

import net.pladema.internation.repository.ips.AllergyIntoleranceRepository;
import net.pladema.internation.repository.ips.entity.AllergyIntolerance;
import net.pladema.internation.repository.masterdata.entity.AllergyIntoleranceClinicalStatus;
import net.pladema.internation.repository.masterdata.entity.AllergyIntoleranceVerificationStatus;
import net.pladema.internation.service.SnomedService;
import net.pladema.internation.service.documents.DocumentService;
import net.pladema.internation.service.documents.anamnesis.AllergyService;
import net.pladema.internation.service.domain.ips.AllergyConditionBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AllergyServiceImpl implements AllergyService {

    private static final Logger LOG = LoggerFactory.getLogger(AllergyServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final AllergyIntoleranceRepository allergyIntoleranceRepository;

    private final DocumentService documentService;

    private final SnomedService snomedService;

    public AllergyServiceImpl(AllergyIntoleranceRepository allergyIntoleranceRepository,
                              DocumentService documentService,
                              SnomedService snomedService){
        this.allergyIntoleranceRepository = allergyIntoleranceRepository;
        this.documentService = documentService;
        this.snomedService = snomedService;
    }

    @Override
    public List<AllergyConditionBo> loadAllergies(Integer patientId, Long documentId, List<AllergyConditionBo> allergies) {
        LOG.debug("Input parameters -> patientId {}, documentId {}, allergies {}", documentId, patientId, allergies);
        allergies.stream().filter(allergy -> allergy.mustSave()).forEach(allergy -> {
            String sctid = snomedService.createSnomedTerm(allergy.getSnomed());
            AllergyIntolerance allergyIntolerance = saveAllergyIntolerance(patientId, allergy, sctid);
            allergy.setId(allergyIntolerance.getId());
            documentService.createDocumentAllergyIntolerance(documentId, allergyIntolerance.getId());
        });
        return allergies.stream().filter(a -> !a.isDeleted()).collect(Collectors.toList());
    }

    private AllergyIntolerance saveAllergyIntolerance(Integer patientId, AllergyConditionBo allergy, String sctid) {
        LOG.debug("Input parameters -> patientId {}, allergy {}, sctid {}", patientId, allergy, sctid);
        AllergyIntolerance allergyIntolerance = new AllergyIntolerance(patientId,
                sctid,
                AllergyIntoleranceClinicalStatus.ACTIVE,
                AllergyIntoleranceVerificationStatus.CONFIRMED,
                allergy.getCategoryId(),
                allergy.getDate(),
                allergy.isDeleted());
        allergyIntolerance = allergyIntoleranceRepository.save(allergyIntolerance);
        LOG.debug("allergyIntolerance saved ->", allergyIntolerance.getId());
        LOG.debug(OUTPUT, allergyIntolerance);
        return allergyIntolerance;
    }

    @Override
    public List<AllergyConditionBo> getAllergiesGeneralState(Integer internmentEpisodeId) {
        LOG.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
        return Collections.emptyList();
    }
}
