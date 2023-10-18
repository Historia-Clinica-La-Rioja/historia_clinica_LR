package net.pladema.questionnaires.edmonton.getsummary.repository;

import net.pladema.questionnaires.common.dto.QuestionnaireSummary;

import java.util.Optional;

public interface EdmontonSummaryRepository {

	Optional<QuestionnaireSummary> getQuestionnaireSummaryReport(Integer questionnaireId);
}
