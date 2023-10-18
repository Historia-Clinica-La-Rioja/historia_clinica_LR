package net.pladema.questionnaires.frail.getsummary.repository;

import net.pladema.questionnaires.common.dto.QuestionnaireSummary;

import java.util.Optional;

public interface FrailSummaryRepository {

	Optional<QuestionnaireSummary> getQuestionnaireSummaryReport(Integer questionnaireId);
}
