package net.pladema.questionnaires.general.getall.domain.service;

import net.pladema.questionnaires.common.dto.QuestionnaireInfo;

import java.util.List;

public interface GetAllService {

	List<QuestionnaireInfo> findQuestionnairesInfo(Integer patientId);
}
