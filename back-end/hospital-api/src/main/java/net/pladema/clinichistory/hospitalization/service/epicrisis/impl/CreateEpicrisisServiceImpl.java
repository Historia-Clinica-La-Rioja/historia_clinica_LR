package net.pladema.clinichistory.hospitalization.service.epicrisis.impl;

import ar.lamansys.sgh.clinichistory.application.createDocument.DocumentFactory;
import ar.lamansys.sgh.clinichistory.domain.ips.ClinicalTermsValidatorUtils;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import net.pladema.clinichistory.hospitalization.repository.domain.InternmentEpisode;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.documents.validation.AnthropometricDataValidator;
import net.pladema.clinichistory.hospitalization.service.documents.validation.EffectiveRiskFactorTimeValidator;
import net.pladema.clinichistory.hospitalization.service.epicrisis.CreateEpicrisisService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.domain.EpicrisisBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Collections;

@Service
public class CreateEpicrisisServiceImpl implements CreateEpicrisisService {

    private static final Logger LOG = LoggerFactory.getLogger(CreateEpicrisisServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final DocumentFactory documentFactory;

    private final InternmentEpisodeService internmentEpisodeService;

    private final DateTimeProvider dateTimeProvider;

    public CreateEpicrisisServiceImpl(DocumentFactory documentFactory,
                                      InternmentEpisodeService internmentEpisodeService,
                                      DateTimeProvider dateTimeProvider) {
        this.documentFactory = documentFactory;
        this.internmentEpisodeService = internmentEpisodeService;
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public EpicrisisBo execute(EpicrisisBo epicrisis) {
        LOG.debug("Input parameters -> epicrisis {}", epicrisis);

        assertContextValid(epicrisis);
        var internmentEpisode = internmentEpisodeService.getInternmentEpisode(epicrisis.getEncounterId(), epicrisis.getInstitutionId());
        epicrisis.setPatientId(internmentEpisode.getPatientId());
        assertInternmentEpisodeCanCreateEpicrisis(internmentEpisode);

        assertEpicrisisValid(epicrisis);
        assertEffectiveRiskFactorTimeValid(epicrisis, internmentEpisode.getEntryDate());
        assertAnthropometricData(epicrisis);

        LocalDateTime now = dateTimeProvider.nowDateTime();
        epicrisis.setPerformedDate(now);

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
        if (ClinicalTermsValidatorUtils.repeatedClinicalTerms(epicrisis.getDiagnosis()))
            throw new ConstraintViolationException("Diagnósticos secundarios repetidos", Collections.emptySet());
        if (ClinicalTermsValidatorUtils.repeatedClinicalTerms(epicrisis.getPersonalHistories()))
            throw new ConstraintViolationException("Antecedentes personales repetidos", Collections.emptySet());
        if (ClinicalTermsValidatorUtils.repeatedClinicalTerms(epicrisis.getFamilyHistories()))
            throw new ConstraintViolationException("Antecedentes familiares repetidos", Collections.emptySet());
    }

    private void assertEffectiveRiskFactorTimeValid(EpicrisisBo epicrisis, LocalDateTime entryDate) {
        var validator = new EffectiveRiskFactorTimeValidator();
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
