package net.pladema.clinichistory.hospitalization.service.anamnesis.impl;

import ar.lamansys.sgh.clinichistory.application.createDocument.DocumentFactory;
import ar.lamansys.sgh.clinichistory.domain.ips.ClinicalTermsValidatorUtils;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosisBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import net.pladema.clinichistory.hospitalization.repository.domain.InternmentEpisode;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.anamnesis.AnamnesisValidator;
import net.pladema.clinichistory.hospitalization.service.anamnesis.CreateAnamnesisService;
import net.pladema.clinichistory.hospitalization.service.anamnesis.domain.AnamnesisBo;
import net.pladema.clinichistory.hospitalization.service.documents.validation.AnthropometricDataValidator;
import net.pladema.clinichistory.hospitalization.service.documents.validation.EffectiveRiskFactorTimeValidator;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
public class CreateAnamnesisServiceImpl implements CreateAnamnesisService {

    private static final Logger LOG = LoggerFactory.getLogger(CreateAnamnesisServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final DocumentFactory documentFactory;

    private final InternmentEpisodeService internmentEpisodeService;

    private final DateTimeProvider dateTimeProvider;

	private final AnamnesisValidator anamnesisValidator;

    public CreateAnamnesisServiceImpl(DocumentFactory documentFactory,
                                      InternmentEpisodeService internmentEpisodeService,
                                      DateTimeProvider dateTimeProvider,
									  AnamnesisValidator anamnesisValidator) {
        this.documentFactory = documentFactory;
        this.internmentEpisodeService = internmentEpisodeService;
        this.dateTimeProvider = dateTimeProvider;
		this.anamnesisValidator = anamnesisValidator;
    }

    @Override
    @Transactional
    public AnamnesisBo execute(AnamnesisBo anamnesis) {
        LOG.debug("Input parameters -> anamnesis {}", anamnesis);

        anamnesisValidator.assertContextValid(anamnesis);
        var internmentEpisode = internmentEpisodeService
                .getInternmentEpisode(anamnesis.getEncounterId(), anamnesis.getInstitutionId());

        LocalDateTime now = dateTimeProvider.nowDateTime();
        anamnesis.setPerformedDate(now);

        anamnesis.setPatientId(internmentEpisode.getPatientId());

		anamnesisValidator.assertAnamnesisValid(anamnesis);
		anamnesisValidator.assertDoesNotHaveAnamnesis(internmentEpisode);
		anamnesisValidator.assertEffectiveRiskFactorTimeValid(anamnesis, internmentEpisode.getEntryDate());
		anamnesisValidator.assertAnthropometricData(anamnesis);

        
        anamnesis.setId(documentFactory.run(anamnesis, true));

        internmentEpisodeService.updateAnamnesisDocumentId(anamnesis.getEncounterId(), anamnesis.getId());
        LOG.debug(OUTPUT, anamnesis);

        return anamnesis;
    }

}
