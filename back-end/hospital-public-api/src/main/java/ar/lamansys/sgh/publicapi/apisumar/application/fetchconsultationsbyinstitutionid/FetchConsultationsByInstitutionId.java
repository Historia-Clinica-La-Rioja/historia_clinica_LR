package ar.lamansys.sgh.publicapi.apisumar.application.fetchconsultationsbyinstitutionid;

import ar.lamansys.sgh.publicapi.apisumar.application.port.out.ConsultationDetailDataStorage;

import ar.lamansys.sgh.publicapi.apisumar.domain.ConsultationDetailDataBo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FetchConsultationsByInstitutionId {

	private final Logger logger;
	private final ConsultationDetailDataStorage consultationDetailDataStorage;

	public FetchConsultationsByInstitutionId(ConsultationDetailDataStorage consultationDetailDataStorage) {
		this.consultationDetailDataStorage = consultationDetailDataStorage;
		this.logger = LoggerFactory.getLogger(FetchConsultationsByInstitutionId.class);
	}

	public List<ConsultationDetailDataBo> run(Integer institutionId) {
		logger.debug("Input parameters -> institutionId {}", institutionId);
		List<ConsultationDetailDataBo> result = consultationDetailDataStorage.getConsultationsData(institutionId);
		logger.debug("Output -> {}", result);
		return result;
	}

}
