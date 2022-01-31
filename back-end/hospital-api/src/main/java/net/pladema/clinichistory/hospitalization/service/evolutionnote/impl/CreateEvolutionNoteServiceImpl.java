package net.pladema.clinichistory.hospitalization.service.evolutionnote.impl;

import ar.lamansys.sgh.clinichistory.application.createDocument.DocumentFactory;
import ar.lamansys.sgh.clinichistory.application.fetchHospitalizationState.FetchHospitalizationHealthConditionState;
import ar.lamansys.sgh.clinichistory.domain.ips.ClinicalTermsValidatorUtils;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosisBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import net.pladema.clinichistory.hospitalization.repository.domain.InternmentEpisode;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.documents.validation.AnthropometricDataValidator;
import net.pladema.clinichistory.hospitalization.service.documents.validation.EffectiveVitalSignTimeValidator;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.CreateEvolutionNoteService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.domain.EvolutionNoteBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.Collections;

@Service
public class CreateEvolutionNoteServiceImpl implements CreateEvolutionNoteService {

    private static final Logger LOG = LoggerFactory.getLogger(CreateEvolutionNoteServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final DocumentFactory documentFactory;

    private final InternmentEpisodeService internmentEpisodeService;

    private final FetchHospitalizationHealthConditionState fetchHospitalizationHealthConditionState;

    private final DateTimeProvider dateTimeProvider;

    public CreateEvolutionNoteServiceImpl(DocumentFactory documentFactory,
                                          InternmentEpisodeService internmentEpisodeService,
                                          FetchHospitalizationHealthConditionState fetchHospitalizationHealthConditionState,
                                          DateTimeProvider dateTimeProvider) {
        this.documentFactory = documentFactory;
        this.internmentEpisodeService = internmentEpisodeService;
        this.fetchHospitalizationHealthConditionState = fetchHospitalizationHealthConditionState;
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public EvolutionNoteBo execute(EvolutionNoteBo evolutionNote) {
        LOG.debug("Input parameters -> evolutionNote {}", evolutionNote);

        assertContextValid(evolutionNote);
        var internmentEpisode = internmentEpisodeService
                .getInternmentEpisode(evolutionNote.getEncounterId(), evolutionNote.getInstitutionId());
        evolutionNote.setPatientId(internmentEpisode.getPatientId());

        LocalDate now = dateTimeProvider.nowDate();
        evolutionNote.setPerformedDate(now);

        assertDoesNotHaveEpicrisis(internmentEpisode);
        assertEvolutionNoteValid(evolutionNote);
        assertEffectiveVitalSignTimeValid(evolutionNote, internmentEpisode.getEntryDate());
        assertDiagnosisValid(evolutionNote, internmentEpisode);
        assertAnthropometricData(evolutionNote);


        evolutionNote.setId(documentFactory.run(evolutionNote, true));

        internmentEpisodeService.addEvolutionNote(internmentEpisode.getId(), evolutionNote.getId());

        LOG.debug(OUTPUT, evolutionNote);

        return evolutionNote;
    }

    private void assertContextValid(EvolutionNoteBo evolutionNote) {
        if (evolutionNote.getInstitutionId() == null)
            throw new ConstraintViolationException("El id de la institución es obligatorio", Collections.emptySet());
        if (evolutionNote.getEncounterId() == null)
            throw new ConstraintViolationException("El id del encuentro asociado es obligatorio", Collections.emptySet());
    }

    private void assertEvolutionNoteValid(EvolutionNoteBo evolutionNote) {
        evolutionNote.validateSelf();
        if (ClinicalTermsValidatorUtils.repeatedClinicalTerms(evolutionNote.getDiagnosis()))
            throw new ConstraintViolationException("Diagnósticos secundarios repetidos", Collections.emptySet());
        if (ClinicalTermsValidatorUtils.repeatedClinicalTerms(evolutionNote.getProcedures()))
            throw new ConstraintViolationException("Procedimientos repetidos", Collections.emptySet());
    }

    private void assertAnthropometricData(EvolutionNoteBo evolutionNoteBo) {
        var validator = new AnthropometricDataValidator();
        validator.isValid(evolutionNoteBo);
    }

    private void assertDoesNotHaveEpicrisis(InternmentEpisode internmentEpisode) {
        if(internmentEpisode.getEpicrisisDocId() != null) {
            throw new ConstraintViolationException("Esta internación ya posee una epicrisis", Collections.emptySet());
        }
    }

    private void assertDiagnosisValid(EvolutionNoteBo evolutionNote, InternmentEpisode internmentEpisode) {
        if (evolutionNote.getDiagnosis() == null || evolutionNote.getDiagnosis().isEmpty())
            return;
        HealthConditionBo mainDiagnosis = fetchHospitalizationHealthConditionState.getMainDiagnosisGeneralState(internmentEpisode.getId());
        if (mainDiagnosis == null)
            return;
        if (evolutionNote.getDiagnosis().stream()
                .map(DiagnosisBo::getSnomed)
                .anyMatch(d -> d.equals(mainDiagnosis.getSnomed()))) {
            throw new ConstraintViolationException("Diagnostico principal duplicado en los secundarios", Collections.emptySet());
        }
    }

    private void assertEffectiveVitalSignTimeValid(EvolutionNoteBo evolutionNote, LocalDate entryDate) {
        var validator = new EffectiveVitalSignTimeValidator();
        validator.isValid(evolutionNote, entryDate);
    }

}
