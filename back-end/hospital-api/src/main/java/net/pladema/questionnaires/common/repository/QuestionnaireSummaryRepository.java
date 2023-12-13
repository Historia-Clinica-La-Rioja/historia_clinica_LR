package net.pladema.questionnaires.common.repository;

import java.util.Optional;

import net.pladema.questionnaires.common.dto.QuestionnaireSummary;

public interface QuestionnaireSummaryRepository {

	Optional<QuestionnaireSummary> getQuestionnaireSummaryReport(Integer questionnaireId);
}
