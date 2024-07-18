package ar.lamansys.sgh.publicapi.apisumar.application.fetchrecipesbysisacode;

import ar.lamansys.sgh.publicapi.apisumar.application.port.out.ConsultationDetailDataStorage;

import ar.lamansys.sgh.publicapi.apisumar.domain.RecipeDetailBo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class FetchRecipesBySisaCode {

	private final Logger logger;
	private final ConsultationDetailDataStorage consultationDetailDataStorage;

	public FetchRecipesBySisaCode(ConsultationDetailDataStorage consultationDetailDataStorage) {
		this.consultationDetailDataStorage = consultationDetailDataStorage;
		this.logger = LoggerFactory.getLogger(FetchRecipesBySisaCode.class);
	}

	public List<RecipeDetailBo> run(String sisaCode, LocalDate start, LocalDate end) {
		logger.debug("Input parameters -> sisaCode {}, startDate {}, endDate {}", sisaCode, start, end);
		var startDate = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 0, 0);
		var endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23, 59, 59, LocalTime.MAX.getNano());

		List<RecipeDetailBo> result = consultationDetailDataStorage.getRecipes(sisaCode, startDate, endDate);
		logger.debug("Output -> {}", result);
		return result;
	}
}
