package net.pladema.clinichistory.hospitalization.service.epicrisis.impl;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.application.createDocument.DocumentFactory;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.CreateEpicrisisService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.EpicrisisValidator;
import net.pladema.clinichistory.hospitalization.service.epicrisis.domain.EpicrisisBo;

@Service
public class CreateEpicrisisServiceImpl implements CreateEpicrisisService {

    private static final Logger LOG = LoggerFactory.getLogger(CreateEpicrisisServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final DocumentFactory documentFactory;

    private final InternmentEpisodeService internmentEpisodeService;

    private final DateTimeProvider dateTimeProvider;

    private final EpicrisisValidator epicrisisValidator;

    public CreateEpicrisisServiceImpl(DocumentFactory documentFactory, InternmentEpisodeService internmentEpisodeService, DateTimeProvider dateTimeProvider, EpicrisisValidator epicrisisValidator) {
        this.documentFactory = documentFactory;
        this.internmentEpisodeService = internmentEpisodeService;
        this.dateTimeProvider = dateTimeProvider;
		this.epicrisisValidator = epicrisisValidator;
    }

    @Override
    public EpicrisisBo execute(EpicrisisBo epicrisis) {
        LOG.debug("Input parameters -> epicrisis {}", epicrisis);

		epicrisisValidator.assertContextValid(epicrisis);
        var internmentEpisode = internmentEpisodeService.getInternmentEpisode(epicrisis.getEncounterId(), epicrisis.getInstitutionId());
        epicrisis.setPatientId(internmentEpisode.getPatientId());
        epicrisisValidator.assertInternmentEpisodeCanCreateEpicrisis(internmentEpisode);

		epicrisisValidator.assertEpicrisisValid(epicrisis);
		epicrisisValidator.assertEffectiveRiskFactorTimeValid(epicrisis, internmentEpisode.getEntryDate());
		epicrisisValidator.assertAnthropometricData(epicrisis);

        LocalDateTime now = dateTimeProvider.nowDateTime();
        epicrisis.setPerformedDate(now);

        epicrisis.setId(documentFactory.run(epicrisis, true));
        internmentEpisodeService.updateEpicrisisDocumentId(internmentEpisode.getId(), epicrisis.getId());

        LOG.debug(OUTPUT, epicrisis);

        return epicrisis;
    }

}
