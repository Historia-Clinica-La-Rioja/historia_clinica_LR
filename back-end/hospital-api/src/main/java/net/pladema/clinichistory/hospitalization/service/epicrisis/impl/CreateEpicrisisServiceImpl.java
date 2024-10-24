package net.pladema.clinichistory.hospitalization.service.epicrisis.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.lamansys.sgh.clinichistory.application.createDocument.DocumentFactory;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.CreateEpicrisisService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.EpicrisisValidator;
import net.pladema.clinichistory.hospitalization.service.epicrisis.domain.EpicrisisBo;

@RequiredArgsConstructor
@Service
public class CreateEpicrisisServiceImpl implements CreateEpicrisisService {

    private static final Logger LOG = LoggerFactory.getLogger(CreateEpicrisisServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final DocumentFactory documentFactory;

    private final InternmentEpisodeService internmentEpisodeService;

    private final DateTimeProvider dateTimeProvider;

    private final EpicrisisValidator epicrisisValidator;

    @Override
	@Transactional
    public EpicrisisBo execute(EpicrisisBo epicrisis, boolean draft) {
        LOG.debug("Input parameters -> epicrisis {}", epicrisis);
		epicrisisValidator.assertContextValid(epicrisis);
        var internmentEpisode = internmentEpisodeService.getInternmentEpisode(epicrisis.getEncounterId(), epicrisis.getInstitutionId());
        epicrisis.setPatientId(internmentEpisode.getPatientId());
        epicrisisValidator.assertInternmentEpisodeCanCreateEpicrisis(internmentEpisode);

		epicrisisValidator.assertEpicrisisValid(epicrisis);
		epicrisisValidator.assertEffectiveRiskFactorTimeValid(epicrisis, internmentEpisode.getEntryDate());
		epicrisisValidator.assertAnthropometricData(epicrisis);

		epicrisis.getMainDiagnosis().setId(null);
		Optional.ofNullable(epicrisis.getDiagnosis()).ifPresent(list->list.forEach(d->d.setId(null)));
		Optional.ofNullable(epicrisis.getPersonalHistories().getContent()).ifPresent(list->list.forEach(d->d.setId(null)));
		Optional.ofNullable(epicrisis.getFamilyHistories().getContent()).ifPresent(list->list.forEach(d->d.setId(null)));
		Optional.ofNullable(epicrisis.getAllergies().getContent()).ifPresent(list->list.forEach(d->d.setId(null)));
		Optional.ofNullable(epicrisis.getProcedures()).ifPresent(list->list.forEach(d->d.setId(null)));
		Optional.ofNullable(epicrisis.getImmunizations()).ifPresent(list->list.forEach(d->d.setId(null)));
		Optional.ofNullable(epicrisis.getOtherProblems()).ifPresent(list->list.forEach(d->d.setId(null)));
		if (epicrisis.getExternalCause() != null) epicrisis.getExternalCause().setId(null);
		if (epicrisis.getObstetricEvent() != null) epicrisis.getObstetricEvent().setId(null);

        LocalDateTime now = dateTimeProvider.nowDateTime();
        epicrisis.setPerformedDate(now);
		epicrisis.setConfirmed(!draft);
		epicrisis.setPatientInternmentAge(internmentEpisodeService.getEntryDate(epicrisis.getEncounterId()).toLocalDate());
        epicrisis.setId(documentFactory.run(epicrisis, epicrisis.isConfirmed()));

        internmentEpisodeService.updateEpicrisisDocumentId(internmentEpisode.getId(), epicrisis.getId());

        LOG.debug(OUTPUT, epicrisis);

        return epicrisis;
    }

}
