package net.pladema.questionnaires.edmonton.get.domain.service;

import java.util.List;
import java.util.Optional;

import net.pladema.questionnaires.common.domain.Answer;
import net.pladema.questionnaires.common.dto.QuestionnaireSummary;

public interface GetEdmontonService {

	List<Answer> findPatientQuestionnaire(Integer patientId);

	Optional<QuestionnaireSummary> findQuestionnaireSummary(Integer questionnaireId);
}
