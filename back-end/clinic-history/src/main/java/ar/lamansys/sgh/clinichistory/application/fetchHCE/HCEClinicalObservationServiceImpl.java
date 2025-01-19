package ar.lamansys.sgh.clinichistory.application.fetchHCE;

import ar.lamansys.sgh.clinichistory.domain.hce.HCEAnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.hce.Last2HCERiskFactorsBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.HCEClinicalObservationRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity.HCEMapClinicalObservationVo;
import java.util.Arrays;
import java.util.List;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity.HCEMapHistoricClinicalObservationVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class HCEClinicalObservationServiceImpl implements HCEClinicalObservationService {

    private static final String LOGGING_OUTPUT = "Output -> {}";
    private static final String LOGGING_INPUT = "Input parameters -> patientId {} ";

    private final HCEClinicalObservationRepository hceClinicalObservationRepository;

    @Override
    public HCEAnthropometricDataBo getLastAnthropometricDataGeneralState(Integer patientId) {
        log.debug(LOGGING_INPUT, patientId);
        List<Short> invalidDocumentTypes = List.of(DocumentType.ANESTHETIC_REPORT);
		HCEMapClinicalObservationVo resultQuery = hceClinicalObservationRepository.getGeneralState(patientId, invalidDocumentTypes);
        HCEAnthropometricDataBo result = resultQuery.getNAnthropometricData(0).orElse(null);
        log.debug(LOGGING_OUTPUT, result);
        return result;
    }

    @Override
	public List<HCEAnthropometricDataBo> getLast2AnthropometricDataGeneralState(Integer patientId) {
		log.debug(LOGGING_INPUT, patientId);
		List<Short> invalidDocumentTypes = List.of(DocumentType.ANESTHETIC_REPORT);
		HCEMapClinicalObservationVo resultQuery = hceClinicalObservationRepository.getGeneralState(patientId, invalidDocumentTypes);
		List<HCEAnthropometricDataBo> result = resultQuery.getLastNAnthropometricData(2);
		log.debug(LOGGING_OUTPUT, result);
		return result;
	}

    @Override
    public Last2HCERiskFactorsBo getLast2RiskFactorsGeneralState(Integer patientId) {
        log.debug(LOGGING_INPUT, patientId);
		List<Short> invalidDocumentTypes = Arrays.asList(DocumentType.ANAMNESIS, DocumentType.EVALUATION_NOTE, DocumentType.EPICRISIS, DocumentType.ANESTHETIC_REPORT);
		HCEMapClinicalObservationVo resultQuery = hceClinicalObservationRepository.getGeneralState(patientId, invalidDocumentTypes);
        Last2HCERiskFactorsBo result = new Last2HCERiskFactorsBo();
        for (int i=0;i<2;i++){
            if (i==0) {
                resultQuery.getLastNRiskFactors(i).ifPresent(result::setCurrent);
            }
            if (i==1) {
                resultQuery.getLastNRiskFactors(i).ifPresent(result::setPrevious);
            }
        }
        log.debug(LOGGING_OUTPUT, result);
        return result;
    }

	@Override
	public List<HCEAnthropometricDataBo> getHistoricAnthropometricData(Integer patientId){
		log.debug(LOGGING_INPUT, patientId);
		List<Short> invalidDocumentTypes = List.of(DocumentType.ANESTHETIC_REPORT);
		HCEMapHistoricClinicalObservationVo resultQuery = hceClinicalObservationRepository.getHistoricData(patientId, invalidDocumentTypes);
		List<HCEAnthropometricDataBo> result = resultQuery.getHistoricAnthropometricData();
		log.debug(LOGGING_OUTPUT, result);
		return result;
	}

}
