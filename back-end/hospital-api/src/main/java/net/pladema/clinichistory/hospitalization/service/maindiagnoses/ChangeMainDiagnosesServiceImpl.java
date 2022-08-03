package net.pladema.clinichistory.hospitalization.service.maindiagnoses;

import ar.lamansys.sgh.clinichistory.application.fetchHospitalizationState.FetchHospitalizationHealthConditionState;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionVerificationStatus;
import ar.lamansys.sgh.clinichistory.application.createDocument.DocumentFactory;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import net.pladema.clinichistory.hospitalization.repository.domain.InternmentEpisode;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.maindiagnoses.domain.MainDiagnosisBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosisBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

@Service
public class ChangeMainDiagnosesServiceImpl implements ChangeMainDiagnosesService {

    private static final Logger LOG = LoggerFactory.getLogger(ChangeMainDiagnosesServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final DocumentFactory documentFactory;

    private final InternmentEpisodeService internmentEpisodeService;

    private final FetchHospitalizationHealthConditionState fetchHospitalizationHealthConditionState;

	private final DateTimeProvider dateTimeProvider;

    public ChangeMainDiagnosesServiceImpl(DocumentFactory documentFactory,
                                          InternmentEpisodeService internmentEpisodeService,
                                          FetchHospitalizationHealthConditionState fetchHospitalizationHealthConditionState,
										  DateTimeProvider dateTimeProvider) {
        this.documentFactory = documentFactory;
        this.internmentEpisodeService = internmentEpisodeService;
        this.fetchHospitalizationHealthConditionState = fetchHospitalizationHealthConditionState;
		this.dateTimeProvider = dateTimeProvider;
    }

    @Override
	@Transactional
    public MainDiagnosisBo execute(MainDiagnosisBo mainDiagnosisBo) {
        LOG.debug("Input parameters -> mainDiagnosisBo {}", mainDiagnosisBo);

        assertContextValid(mainDiagnosisBo);
        var internmentEpisode = internmentEpisodeService.
                getInternmentEpisode(mainDiagnosisBo.getEncounterId(), mainDiagnosisBo.getInstitutionId());
        mainDiagnosisBo.setPatientId(internmentEpisode.getPatientId());

		LocalDateTime now = dateTimeProvider.nowDateTime();
		mainDiagnosisBo.setPerformedDate(now);

        assertDoesNotHaveEpicrisis(internmentEpisode);
        mainDiagnosisBo.validateSelf();
		mainDiagnosisBo.getMainDiagnosis().setId(null);
        HealthConditionBo currentMainDiagnose = fetchHospitalizationHealthConditionState.
                getMainDiagnosisGeneralState(internmentEpisode.getId());
        if (!currentMainDiagnose.getSnomed().equals(mainDiagnosisBo.getMainDiagnosis().getSnomed()))
            mainDiagnosisBo.setDiagnosis(Arrays.asList(createAlternativeDiagnoses(currentMainDiagnose)));

        mainDiagnosisBo.getMainDiagnosis().setVerificationId(ConditionVerificationStatus.CONFIRMED);
        mainDiagnosisBo.setId(documentFactory.run(mainDiagnosisBo, true));

        internmentEpisodeService.addEvolutionNote(internmentEpisode.getId(), mainDiagnosisBo.getId());


        LOG.debug(OUTPUT, mainDiagnosisBo);

        return mainDiagnosisBo;
    }

    private void assertContextValid(MainDiagnosisBo mainDiagnosisBo) {
        if (mainDiagnosisBo.getInstitutionId() == null)
            throw new ConstraintViolationException("El id de la institución es obligatorio", Collections.emptySet());
        if (mainDiagnosisBo.getEncounterId() == null)
            throw new ConstraintViolationException("El id del encuentro asociado es obligatorio", Collections.emptySet());
    }

    private DiagnosisBo createAlternativeDiagnoses(HealthConditionBo currentMainDiagnose) {
        LOG.debug("Downgrade main diagnosis {} to alternative diagnosis",  currentMainDiagnose);
        DiagnosisBo diagnosisBo = new DiagnosisBo();
        diagnosisBo.setMain(false);
        diagnosisBo.setSnomed(currentMainDiagnose.getSnomed());
        diagnosisBo.setStatus(currentMainDiagnose.getStatus());
        diagnosisBo.setStatusId(currentMainDiagnose.getStatusId());
        diagnosisBo.setVerification(currentMainDiagnose.getVerification());
        diagnosisBo.setVerificationId(currentMainDiagnose.getVerificationId());
        return diagnosisBo;
    }

    private void assertDoesNotHaveEpicrisis(InternmentEpisode internmentEpisode) {
        if(internmentEpisodeService.haveEpicrisis(internmentEpisode.getId())) {
            throw new ConstraintViolationException("Esta internación ya posee una epicrisis", Collections.emptySet());
        }
    }

}
