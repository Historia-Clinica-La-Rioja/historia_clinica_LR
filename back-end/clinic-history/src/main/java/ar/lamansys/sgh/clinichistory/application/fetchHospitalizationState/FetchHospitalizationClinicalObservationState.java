package ar.lamansys.sgh.clinichistory.application.fetchHospitalizationState;

import ar.lamansys.sgh.clinichistory.domain.ips.AnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.ips.Last2RiskFactorsBo;
import ar.lamansys.sgh.clinichistory.domain.ips.MapClinicalObservationVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.HCHClinicalObservationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class FetchHospitalizationClinicalObservationState {

    public static final String INPUT_PARAMETERS_INTERNMENT_EPISODE_ID = "Input parameters -> internmentEpisodeId {}";
    public static final String OUTPUT = "Output -> {}";

    private final HCHClinicalObservationRepository clinicalObservationRepository;

    public AnthropometricDataBo getLastAnthropometricDataGeneralState(Integer internmentEpisodeId) {
        log.debug(INPUT_PARAMETERS_INTERNMENT_EPISODE_ID, internmentEpisodeId);
        List<Short> invalidDocumentTypes = List.of(DocumentType.ANESTHETIC_REPORT);
        MapClinicalObservationVo resultQuery = clinicalObservationRepository.getGeneralState(internmentEpisodeId, invalidDocumentTypes);
        AnthropometricDataBo result = resultQuery.getNAnthropometricData(0).orElse(null);
        log.debug(OUTPUT, result);
        return result;
    }

	public List<AnthropometricDataBo> getLast2AnthropometricDataGeneralState(Integer internmentEpisodeId) {
		log.debug(INPUT_PARAMETERS_INTERNMENT_EPISODE_ID, internmentEpisodeId);
        List<Short> invalidDocumentTypes = List.of(DocumentType.ANESTHETIC_REPORT);
		MapClinicalObservationVo resultQuery = clinicalObservationRepository.getGeneralState(internmentEpisodeId, invalidDocumentTypes);
		List<AnthropometricDataBo> result = resultQuery.getLastNAnthropometricData(2);
		log.debug(OUTPUT, result);
		return result;
	}

	public Last2RiskFactorsBo getLast2RiskFactorsGeneralState(Integer internmentEpisodeId) {
        log.debug(INPUT_PARAMETERS_INTERNMENT_EPISODE_ID, internmentEpisodeId);
        List<Short> invalidDocumentTypes = List.of(DocumentType.ANESTHETIC_REPORT);
        MapClinicalObservationVo resultQuery = clinicalObservationRepository.getGeneralState(internmentEpisodeId, invalidDocumentTypes);
        Last2RiskFactorsBo result = new Last2RiskFactorsBo();
        for (int i=0;i<2;i++){
            if (i==0) {
                resultQuery.getLastNRiskFactors(i).ifPresent(result::setCurrent);
            }
            if (i==1) {
                resultQuery.getLastNRiskFactors(i).ifPresent(result::setPrevious);
            }
        }
        log.debug(OUTPUT, result);
        return result;
    }

}
