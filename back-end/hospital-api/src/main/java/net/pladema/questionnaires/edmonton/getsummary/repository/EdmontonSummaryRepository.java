package net.pladema.questionnaires.edmonton.getsummary.repository;

import java.util.Optional;

import net.pladema.questionnaires.common.dto.QuestionnaireSummary;

public interface EdmontonSummaryRepository {

	Optional<QuestionnaireSummary> getQuestionnaireSummaryReport(Integer questionnaireId);
}
