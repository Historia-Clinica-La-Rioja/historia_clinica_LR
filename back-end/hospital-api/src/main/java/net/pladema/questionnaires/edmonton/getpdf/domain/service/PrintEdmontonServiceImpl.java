package net.pladema.questionnaires.edmonton.getpdf.domain.service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import net.pladema.questionnaires.common.domain.Answer;
import net.pladema.questionnaires.common.dto.PrintQuestionnaireDTO;
import net.pladema.questionnaires.common.repository.PrintQuestionnaireRepository;
import net.pladema.questionnaires.frail.getpdf.domain.service.PrintFrailServiceImpl;

@Service
public class PrintEdmontonServiceImpl implements PrintEdmontonService {

	public static final String QUESTIONNAIRE_NOT_FOUND = "questionnaire_not_found";
	public static final String OUTPUT = "output -> {}";
	private final Logger logger = LoggerFactory.getLogger(PrintFrailServiceImpl.class);
	private final PrintQuestionnaireRepository printQuestionnaireRepository;

	public PrintEdmontonServiceImpl(PrintQuestionnaireRepository printQuestionnaireRepository) {
		this.printQuestionnaireRepository = printQuestionnaireRepository;
	}

	@Override
	public List<PrintQuestionnaireDTO> getPrintQuestionnaire(Long questionnaireId) {
		logger.debug("input parameters -> questionnaireId {}", questionnaireId);
		List<Answer> result = printQuestionnaireRepository.getQuestionnaireReportInfo(questionnaireId).orElseThrow(() -> new NotFoundException("bad_questionnaireId", QUESTIONNAIRE_NOT_FOUND));

		if (result != null && result.isEmpty())
			throw new NotFoundException("bad_questionnaireId", QUESTIONNAIRE_NOT_FOUND);
		assert result != null;
		return cast(result);
	}

	@Override
	public Map<String, Object> createQuestionnaireContext(List<PrintQuestionnaireDTO> lst) {
		Map<String, Object> ctx = new HashMap<>();
		ctx.put("answers", lst);
		return ctx;
	}

	@Override
	public String createQuestionnaireFileName(Long questionnaireId, ZonedDateTime questionnaireDate) {
		logger.debug("input parameters -> questionnaireId {}, questionnaireDate {}", questionnaireId, questionnaireDate);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH_mm_ss");
		String formattedDate = questionnaireDate.format(formatter);
		//String outputFileName = String.format("Frail - ID: %s - Fecha: %s.pdf", questionnaireId, questionnaireDate);
		String outputFileName = String.format("Prueba de Edmonton - %s - %s.pdf", questionnaireId, formattedDate);logger.debug(OUTPUT, outputFileName);
		logger.debug(OUTPUT, outputFileName);
		return outputFileName;
	}

	private List<PrintQuestionnaireDTO> cast(List<Answer> lst) {
		List<PrintQuestionnaireDTO> lstCast = new ArrayList<>();
		for (Answer reg : lst) {
			lstCast.add(new PrintQuestionnaireDTO(reg));
		}
		return lstCast;
	}
}
