package net.pladema.clinichistory.ips.service.impl;

import net.pladema.clinichistory.ips.controller.mapper.AllergyConditionMapper;
import net.pladema.clinichistory.ips.repository.AllergyIntoleranceRepository;
import net.pladema.clinichistory.ips.repository.entity.AllergyIntolerance;
import net.pladema.clinichistory.ips.service.domain.AllergyConditionBo;
import net.pladema.clinichistory.ips.service.SnomedService;
import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.ips.service.AllergyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllergyServiceImpl implements AllergyService {

    private static final Logger LOG = LoggerFactory.getLogger(AllergyServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final AllergyIntoleranceRepository allergyIntoleranceRepository;

    private final AllergyConditionMapper allergyConditionMapper;

    private final DocumentService documentService;

    private final SnomedService snomedService;

    public AllergyServiceImpl(AllergyIntoleranceRepository allergyIntoleranceRepository,
                              AllergyConditionMapper allergyConditionMapper,
                              DocumentService documentService,
                              SnomedService snomedService){
        this.allergyIntoleranceRepository = allergyIntoleranceRepository;
        this.allergyConditionMapper = allergyConditionMapper;
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
            allergy.setStatusId(allergyIntolerance.getStatusId());
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

    @Override
    public List<AllergyConditionBo> getAllergiesGeneralState(Integer internmentEpisodeId) {
        LOG.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
        List<AllergyConditionBo> result = allergyConditionMapper.toListAllergyConditionBo(
                allergyIntoleranceRepository.findGeneralState(internmentEpisodeId));
        LOG.debug(OUTPUT, result);
        return result;
    }
}
