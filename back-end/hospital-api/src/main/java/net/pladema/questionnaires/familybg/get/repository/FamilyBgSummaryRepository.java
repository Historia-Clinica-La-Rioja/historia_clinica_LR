package net.pladema.questionnaires.familybg.get.repository;

import net.pladema.questionnaires.common.dto.QuestionnaireSummary;

import java.util.Optional;

public interface FamilyBgSummaryRepository {

	Optional<QuestionnaireSummary> getQuestionnaireSummaryReport(Integer questionnaireId);
}
