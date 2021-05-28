package net.pladema.clinichistory.documents.core.generalstate;

import net.pladema.clinichistory.documents.service.generalstate.ClinicalObservationGeneralStateService;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.generalstate.HCHClinicalObservationRepository;
import net.pladema.clinichistory.hospitalization.service.domain.Last2VitalSignsBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.ips.MapClinicalObservationVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ClinicalObservationGeneralStateServiceImpl implements ClinicalObservationGeneralStateService {

    private static final Logger LOG = LoggerFactory.getLogger(ClinicalObservationGeneralStateServiceImpl.class);

    public static final String INPUT_PARAMETERS_INTERNMENT_EPISODE_ID = "Input parameters -> internmentEpisodeId {}";
    public static final String OUTPUT = "Output -> {}";

    private final HCHClinicalObservationRepository clinicalObservationRepository;

    public ClinicalObservationGeneralStateServiceImpl(HCHClinicalObservationRepository clinicalObservationRepository) {
        this.clinicalObservationRepository = clinicalObservationRepository;
    }

    @Override
    public AnthropometricDataBo getLastAnthropometricDataGeneralState(Integer internmentEpisodeId) {
        LOG.debug(INPUT_PARAMETERS_INTERNMENT_EPISODE_ID, internmentEpisodeId);
        MapClinicalObservationVo resultQuery = clinicalObservationRepository.getGeneralState(internmentEpisodeId);
        AnthropometricDataBo result = resultQuery.getLastNAnthropometricData(0).orElse(null);
        LOG.debug(OUTPUT, result);
        return result;
    }


    @Override
    public Last2VitalSignsBo getLast2VitalSignsGeneralState(Integer internmentEpisodeId) {
        LOG.debug(INPUT_PARAMETERS_INTERNMENT_EPISODE_ID, internmentEpisodeId);
        MapClinicalObservationVo resultQuery = clinicalObservationRepository.getGeneralState(internmentEpisodeId);
        Last2VitalSignsBo result = new Last2VitalSignsBo();
        for (int i=0;i<2;i++){
            if (i==0) {
                resultQuery.getLastNVitalSigns(i).ifPresent(result::setCurrent);
            }
            if (i==1) {
                resultQuery.getLastNVitalSigns(i).ifPresent(result::setPrevious);
            }
        }
        LOG.debug(OUTPUT, result);
        return result;
    }

}
