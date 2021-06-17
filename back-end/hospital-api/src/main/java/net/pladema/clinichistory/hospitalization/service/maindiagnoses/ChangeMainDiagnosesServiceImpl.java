package net.pladema.clinichistory.hospitalization.service.maindiagnoses;

import net.pladema.clinichistory.documents.service.DocumentFactory;
import net.pladema.clinichistory.hospitalization.repository.domain.InternmentEpisode;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.documents.service.generalstate.HealthConditionGeneralStateService;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.ConditionVerificationStatus;
import net.pladema.clinichistory.hospitalization.service.maindiagnoses.domain.MainDiagnosisBo;
import net.pladema.clinichistory.documents.service.ips.domain.DiagnosisBo;
import net.pladema.clinichistory.documents.service.ips.domain.HealthConditionBo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.Collections;

@Service
public class ChangeMainDiagnosesServiceImpl implements ChangeMainDiagnosesService {

    private static final Logger LOG = LoggerFactory.getLogger(ChangeMainDiagnosesServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final DocumentFactory documentFactory;

    private final InternmentEpisodeService internmentEpisodeService;

    private final HealthConditionGeneralStateService healthConditionGeneralStateService;

    public ChangeMainDiagnosesServiceImpl(DocumentFactory documentFactory,
                                          InternmentEpisodeService internmentEpisodeService,
                                          HealthConditionGeneralStateService healthConditionGeneralStateService) {
        this.documentFactory = documentFactory;
        this.internmentEpisodeService = internmentEpisodeService;
        this.healthConditionGeneralStateService = healthConditionGeneralStateService;
    }

    @Override
    public MainDiagnosisBo execute(MainDiagnosisBo mainDiagnosisBo) {
        LOG.debug("Input parameters -> mainDiagnosisBo {}", mainDiagnosisBo);

        assertContextValid(mainDiagnosisBo);
        var internmentEpisode = internmentEpisodeService.
                getInternmentEpisode(mainDiagnosisBo.getEncounterId(), mainDiagnosisBo.getInstitutionId());
        mainDiagnosisBo.setPatientId(internmentEpisode.getPatientId());

        assertDoesNotHaveEpicrisis(internmentEpisode);
        mainDiagnosisBo.validateSelf();

        HealthConditionBo currentMainDiagnose = healthConditionGeneralStateService.
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
        if(internmentEpisode.getEpicrisisDocId() != null) {
            throw new ConstraintViolationException("Esta internación ya posee una epicrisis", Collections.emptySet());
        }
    }

}
