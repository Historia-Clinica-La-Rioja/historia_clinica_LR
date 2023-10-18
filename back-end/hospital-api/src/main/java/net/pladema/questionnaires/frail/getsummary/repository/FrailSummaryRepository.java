package net.pladema.questionnaires.frail.getsummary.repository;

import java.util.Optional;

import net.pladema.questionnaires.common.dto.QuestionnaireSummary;

public interface FrailSummaryRepository {

	Optional<QuestionnaireSummary> getQuestionnaireSummaryReport(Integer questionnaireId);
}
