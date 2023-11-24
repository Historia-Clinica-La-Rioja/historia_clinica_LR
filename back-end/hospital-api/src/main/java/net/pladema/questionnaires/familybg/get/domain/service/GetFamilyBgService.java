package net.pladema.questionnaires.familybg.get.domain.service;

import net.pladema.questionnaires.common.domain.Answer;
import net.pladema.questionnaires.common.dto.QuestionnaireSummary;

import java.util.List;
import java.util.Optional;

public interface GetFamilyBgService {

	List<Answer> findPatientQuestionnaire(Integer patientId);

	Optional<QuestionnaireSummary> findQuestionnaireSummary(Integer questionnaireId);
}
