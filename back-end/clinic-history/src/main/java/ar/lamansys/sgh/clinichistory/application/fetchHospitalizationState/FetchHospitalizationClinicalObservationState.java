package ar.lamansys.sgh.clinichistory.application.fetchHospitalizationState;

import ar.lamansys.sgh.clinichistory.domain.ips.Last2RiskFactorsBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.HCHClinicalObservationRepository;
import ar.lamansys.sgh.clinichistory.domain.ips.AnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.ips.MapClinicalObservationVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FetchHospitalizationClinicalObservationState {

    private static final Logger LOG = LoggerFactory.getLogger(FetchHospitalizationClinicalObservationState.class);

    public static final String INPUT_PARAMETERS_INTERNMENT_EPISODE_ID = "Input parameters -> internmentEpisodeId {}";
    public static final String OUTPUT = "Output -> {}";

    private final HCHClinicalObservationRepository clinicalObservationRepository;

    public FetchHospitalizationClinicalObservationState(HCHClinicalObservationRepository clinicalObservationRepository) {
        this.clinicalObservationRepository = clinicalObservationRepository;
    }

    public AnthropometricDataBo getLastAnthropometricDataGeneralState(Integer internmentEpisodeId) {
        LOG.debug(INPUT_PARAMETERS_INTERNMENT_EPISODE_ID, internmentEpisodeId);
        MapClinicalObservationVo resultQuery = clinicalObservationRepository.getGeneralState(internmentEpisodeId);
        AnthropometricDataBo result = resultQuery.getNAnthropometricData(0).orElse(null);
        LOG.debug(OUTPUT, result);
        return result;
    }

	public List<AnthropometricDataBo> getLast2AnthropometricDataGeneralState(Integer internmentEpisodeId) {
		LOG.debug(INPUT_PARAMETERS_INTERNMENT_EPISODE_ID, internmentEpisodeId);
		MapClinicalObservationVo resultQuery = clinicalObservationRepository.getGeneralState(internmentEpisodeId);
		List<AnthropometricDataBo> result = resultQuery.getLastNAnthropometricData(2);
		LOG.debug(OUTPUT, result);
		return result;
	}

	public Last2RiskFactorsBo getLast2RiskFactorsGeneralState(Integer internmentEpisodeId) {
        LOG.debug(INPUT_PARAMETERS_INTERNMENT_EPISODE_ID, internmentEpisodeId);
        MapClinicalObservationVo resultQuery = clinicalObservationRepository.getGeneralState(internmentEpisodeId);
        Last2RiskFactorsBo result = new Last2RiskFactorsBo();
        for (int i=0;i<2;i++){
            if (i==0) {
                resultQuery.getLastNRiskFactors(i).ifPresent(result::setCurrent);
            }
            if (i==1) {
                resultQuery.getLastNRiskFactors(i).ifPresent(result::setPrevious);
            }
        }
        LOG.debug(OUTPUT, result);
        return result;
    }

}
