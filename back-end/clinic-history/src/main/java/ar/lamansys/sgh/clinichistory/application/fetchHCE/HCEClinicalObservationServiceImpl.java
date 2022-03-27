package ar.lamansys.sgh.clinichistory.application.fetchHCE;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.HCEClinicalObservationRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity.HCEMapClinicalObservationVo;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEAnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.hce.Last2HCERiskFactorsBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class HCEClinicalObservationServiceImpl implements HCEClinicalObservationService {

    private static final Logger LOG = LoggerFactory.getLogger(HCEClinicalObservationServiceImpl.class);

    private static final String LOGGING_OUTPUT = "Output -> {}";
    private static final String LOGGING_INPUT = "Input parameters -> patientId {} ";

    private final HCEClinicalObservationRepository hceClinicalObservationRepository;

    public HCEClinicalObservationServiceImpl(HCEClinicalObservationRepository hceClinicalObservationRepository) {
        this.hceClinicalObservationRepository = hceClinicalObservationRepository;
    }


    @Override
    public HCEAnthropometricDataBo getLastAnthropometricDataGeneralState(Integer patientId) {
        LOG.debug(LOGGING_INPUT, patientId);
        List<Short> invalidDocumentTypes = Arrays.asList();
		HCEMapClinicalObservationVo resultQuery = hceClinicalObservationRepository.getGeneralState(patientId, invalidDocumentTypes);
        HCEAnthropometricDataBo result = resultQuery.getLastNAnthropometricData(0).orElse(null);
        LOG.debug(LOGGING_OUTPUT, result);
        return result;
    }


    @Override
    public Last2HCERiskFactorsBo getLast2RiskFactorsGeneralState(Integer patientId) {
        LOG.debug(LOGGING_INPUT, patientId);
		List<Short> invalidDocumentTypes = Arrays.asList(DocumentType.ANAMNESIS, DocumentType.EVALUATION_NOTE, DocumentType.EPICRISIS);
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
        LOG.debug(LOGGING_OUTPUT, result);
        return result;
    }
}
