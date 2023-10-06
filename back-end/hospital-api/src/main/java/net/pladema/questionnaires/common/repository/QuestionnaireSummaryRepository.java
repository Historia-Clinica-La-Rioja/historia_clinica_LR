package net.pladema.questionnaires.common.repository;

import net.pladema.questionnaires.common.dto.QuestionnaireSummary;

import java.util.Optional;

public interface QuestionnaireSummaryRepository {

	Optional<QuestionnaireSummary> getQuestionnaireSummaryReport(Integer questionnaireId);
}
