package net.pladema.questionnaires.familybg.getpdf.domain.service;

import net.pladema.questionnaires.common.dto.PrintQuestionnaireDTO;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

public interface PrintFamilyBgService {

	List<PrintQuestionnaireDTO> getPrintQuestionnaire(Long questionnaireId);

	Map<String, Object> createQuestionnaireContext(List <PrintQuestionnaireDTO> lst);

	String createQuestionnaireFileName(Long questionnaireId, ZonedDateTime questionnaireDate);
}
