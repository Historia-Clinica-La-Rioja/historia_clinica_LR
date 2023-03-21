package net.pladema.clinichistory.hospitalization.service.evolutionnote.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.lamansys.sgh.clinichistory.application.createDocument.DocumentFactory;
import ar.lamansys.sgh.clinichistory.application.fetchHospitalizationState.FetchHospitalizationHealthConditionState;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.CreateEvolutionNoteService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.EvolutionNoteValidator;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.domain.EvolutionNoteBo;

@Service
public class CreateEvolutionNoteServiceImpl implements CreateEvolutionNoteService {

    private static final Logger LOG = LoggerFactory.getLogger(CreateEvolutionNoteServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final DocumentFactory documentFactory;

    private final InternmentEpisodeService internmentEpisodeService;

    private final FetchHospitalizationHealthConditionState fetchHospitalizationHealthConditionState;

    private final DateTimeProvider dateTimeProvider;

	private final EvolutionNoteValidator evolutionNoteValidator;

    public CreateEvolutionNoteServiceImpl(DocumentFactory documentFactory,
										  InternmentEpisodeService internmentEpisodeService,
										  FetchHospitalizationHealthConditionState fetchHospitalizationHealthConditionState,
										  DateTimeProvider dateTimeProvider,
										  EvolutionNoteValidator evolutionNoteValidator) {
        this.documentFactory = documentFactory;
        this.internmentEpisodeService = internmentEpisodeService;
        this.fetchHospitalizationHealthConditionState = fetchHospitalizationHealthConditionState;
        this.dateTimeProvider = dateTimeProvider;
		this.evolutionNoteValidator = evolutionNoteValidator;
	}

    @Override
	@Transactional
    public EvolutionNoteBo execute(EvolutionNoteBo evolutionNote) {
        LOG.debug("Input parameters -> evolutionNote {}", evolutionNote);
		Optional.ofNullable(evolutionNote.getMainDiagnosis()).ifPresent(md -> md.setId(null));
		Optional.ofNullable(evolutionNote.getDiagnosis()).ifPresent(diagnosis->diagnosis.forEach(d->d.setId(null)));
		evolutionNoteValidator.assertContextValid(evolutionNote);
		evolutionNoteValidator.validateNursePermissionToLoadProcedures(evolutionNote);

        var internmentEpisode = internmentEpisodeService
                .getInternmentEpisode(evolutionNote.getEncounterId(), evolutionNote.getInstitutionId());
        evolutionNote.setPatientId(internmentEpisode.getPatientId());

        LocalDateTime now = dateTimeProvider.nowDateTime();
        evolutionNote.setPerformedDate(now);

		evolutionNoteValidator.assertDoesNotHaveEpicrisis(internmentEpisode.getId());
		evolutionNoteValidator.assertEvolutionNoteValid(evolutionNote);
		evolutionNoteValidator.assertEffectiveRiskFactorTimeValid(evolutionNote, internmentEpisode.getEntryDate());
		evolutionNoteValidator.assertAnthropometricData(evolutionNote);

		HealthConditionBo mainDiagnosis = fetchHospitalizationHealthConditionState.getMainDiagnosisGeneralState(internmentEpisode.getId());
		if (mainDiagnosis != null)
			evolutionNoteValidator.assertDiagnosisValid(evolutionNote, mainDiagnosis);

		evolutionNote = evolutionNoteValidator.verifyEvolutionNoteDiagnosis(evolutionNote, mainDiagnosis);

		evolutionNote.setPatientInternmentAge(internmentEpisodeService.getEntryDate(evolutionNote.getEncounterId()).toLocalDate());

        evolutionNote.setId(documentFactory.run(evolutionNote, true));

        internmentEpisodeService.addEvolutionNote(internmentEpisode.getId(), evolutionNote.getId());

        LOG.debug(OUTPUT, evolutionNote);

        return evolutionNote;
    }

}
