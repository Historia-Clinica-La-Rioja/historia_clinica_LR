package ar.lamansys.sgh.publicapi.apisumar.application.fetchconsultationsbysisacode;

import ar.lamansys.sgh.publicapi.apisumar.application.port.out.ConsultationDetailDataStorage;

import ar.lamansys.sgh.publicapi.apisumar.domain.ConsultationDetailDataBo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FetchConsultationsBySisaCode {

	private final Logger logger;
	private final ConsultationDetailDataStorage consultationDetailDataStorage;

	public FetchConsultationsBySisaCode(ConsultationDetailDataStorage consultationDetailDataStorage) {
		this.consultationDetailDataStorage = consultationDetailDataStorage;
		this.logger = LoggerFactory.getLogger(FetchConsultationsBySisaCode.class);
	}

	public List<ConsultationDetailDataBo> run(String sisaCode) {
		logger.debug("Input parameters -> sisaCode {}", sisaCode);
		List<ConsultationDetailDataBo> result = consultationDetailDataStorage.getConsultationsData(sisaCode);
		logger.debug("Output -> {}", result);
		return result;
	}

}
