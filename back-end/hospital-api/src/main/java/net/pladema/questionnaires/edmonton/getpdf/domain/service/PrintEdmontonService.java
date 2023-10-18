package net.pladema.questionnaires.edmonton.getpdf.domain.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import net.pladema.questionnaires.common.dto.PrintQuestionnaireDTO;

public interface PrintEdmontonService {

	List<PrintQuestionnaireDTO> getPrintQuestionnaire(Long questionnaireId);

	Map<String, Object> createQuestionnaireContext(List <PrintQuestionnaireDTO> lst);

	String createQuestionnaireFileName(Long questionnaireId, ZonedDateTime questionnaireDate);

}
