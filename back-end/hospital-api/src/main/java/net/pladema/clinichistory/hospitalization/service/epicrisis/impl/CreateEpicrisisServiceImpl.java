package net.pladema.clinichistory.hospitalization.service.epicrisis.impl;

import net.pladema.clinichistory.documents.service.DocumentFactory;
import net.pladema.clinichistory.documents.service.ips.domain.ClinicalTerm;
import net.pladema.clinichistory.documents.service.ips.domain.SnomedBo;
import net.pladema.clinichistory.hospitalization.repository.domain.InternmentEpisode;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.documents.validation.AnthropometricDataValidator;
import net.pladema.clinichistory.hospitalization.service.documents.validation.EffectiveVitalSignTimeValidator;
import net.pladema.clinichistory.hospitalization.service.epicrisis.CreateEpicrisisService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.domain.EpicrisisBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CreateEpicrisisServiceImpl implements CreateEpicrisisService {

    private static final Logger LOG = LoggerFactory.getLogger(CreateEpicrisisServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final DocumentFactory documentFactory;

    private final InternmentEpisodeService internmentEpisodeService;

    public CreateEpicrisisServiceImpl(DocumentFactory documentFactory,
                                      InternmentEpisodeService internmentEpisodeService) {
        this.documentFactory = documentFactory;
        this.internmentEpisodeService = internmentEpisodeService;
    }

    @Override
    public EpicrisisBo execute(EpicrisisBo epicrisis) {
        LOG.debug("Input parameters -> epicrisis {}", epicrisis);

        assertContextValid(epicrisis);
        var internmentEpisode = internmentEpisodeService.getInternmentEpisode(epicrisis.getEncounterId(), epicrisis.getInstitutionId());
        epicrisis.setPatientId(internmentEpisode.getPatientId());
        assertInternmentEpisodeCanCreateEpicrisis(internmentEpisode);

        assertEpicrisisValid(epicrisis);
        assertEffectiveVitalSignTimeValid(epicrisis, internmentEpisode.getEntryDate());
        assertAnthropometricData(epicrisis);

        epicrisis.setId(documentFactory.run(epicrisis, true));
        internmentEpisodeService.updateEpicrisisDocumentId(internmentEpisode.getId(), epicrisis.getId());

        LOG.debug(OUTPUT, epicrisis);

        return epicrisis;
    }

    private void assertContextValid(EpicrisisBo epicrisis) {
        if (epicrisis.getInstitutionId() == null)
            throw new ConstraintViolationException("El id de la institución es obligatorio", Collections.emptySet());
        if (epicrisis.getEncounterId() == null)
            throw new ConstraintViolationException("El id del encuentro asociado es obligatorio", Collections.emptySet());
    }

    private void assertEpicrisisValid(EpicrisisBo epicrisis) {
        epicrisis.validateSelf();
        if (repeatedClinicalTerms(epicrisis.getDiagnosis()))
            throw new ConstraintViolationException("Diagnósticos secundarios repetidos", Collections.emptySet());
        if (repeatedClinicalTerms(epicrisis.getPersonalHistories()))
            throw new ConstraintViolationException("Antecedentes personales repetidos", Collections.emptySet());
        if (repeatedClinicalTerms(epicrisis.getFamilyHistories()))
            throw new ConstraintViolationException("Antecedentes familiares repetidos", Collections.emptySet());
    }

    private boolean repeatedClinicalTerms(List<? extends ClinicalTerm> clinicalTerms) {
        if (clinicalTerms == null || clinicalTerms.isEmpty())
            return false;
        final Set<SnomedBo> set = new HashSet<>();
        for (ClinicalTerm ct : clinicalTerms)
            if (!set.add(ct.getSnomed()))
                return true;
        return false;
    }

    private void assertEffectiveVitalSignTimeValid(EpicrisisBo epicrisis, LocalDate entryDate) {
        var validator = new EffectiveVitalSignTimeValidator();
        validator.isValid(epicrisis, entryDate);
    }

    private void assertAnthropometricData(EpicrisisBo epicrisis) {
        var validator = new AnthropometricDataValidator();
        validator.isValid(epicrisis);
    }

    private void assertInternmentEpisodeCanCreateEpicrisis(InternmentEpisode internmentEpisode) {
        if(!internmentEpisodeService.canCreateEpicrisis(internmentEpisode.getId())) {
            throw new ConstraintViolationException("Esta internación no puede crear una epicrisis", Collections.emptySet());
        }
    }
}
