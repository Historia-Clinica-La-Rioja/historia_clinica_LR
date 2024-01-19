package net.pladema.questionnaires.frail.get.repository;

import java.util.Optional;

import net.pladema.questionnaires.common.dto.QuestionnaireSummary;

public interface FrailSummaryRepository {

	Optional<QuestionnaireSummary> getQuestionnaireSummaryReport(Integer questionnaireId);
}
