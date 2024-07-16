package ar.lamansys.sgh.publicapi.apisumar.application.fetchconsultationsbysisacode;

import ar.lamansys.sgh.publicapi.apisumar.application.port.out.ConsultationDetailDataStorage;

import ar.lamansys.sgh.publicapi.apisumar.domain.ConsultationDetailDataBo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class FetchConsultationsBySisaCode {

	private final Logger logger;
	private final ConsultationDetailDataStorage consultationDetailDataStorage;

	public FetchConsultationsBySisaCode(ConsultationDetailDataStorage consultationDetailDataStorage) {
		this.consultationDetailDataStorage = consultationDetailDataStorage;
		this.logger = LoggerFactory.getLogger(FetchConsultationsBySisaCode.class);
	}

	public List<ConsultationDetailDataBo> run(String sisaCode, LocalDate start, LocalDate end) {
		logger.debug("Input parameters -> sisaCode {}", sisaCode);
		var startDate = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 0, 0);
		var endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23, 59, 59, LocalTime.MAX.getNano());

		List<ConsultationDetailDataBo> result = consultationDetailDataStorage.getConsultationsData(sisaCode, startDate, endDate);
		logger.debug("Output -> {}", result);
		return result;
	}

}
