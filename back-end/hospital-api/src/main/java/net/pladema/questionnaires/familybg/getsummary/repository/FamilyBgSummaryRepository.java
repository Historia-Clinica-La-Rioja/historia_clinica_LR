package net.pladema.questionnaires.familybg.getsummary.repository;

import net.pladema.questionnaires.common.dto.QuestionnaireSummary;

import java.util.Optional;

public interface FamilyBgSummaryRepository {

	Optional<QuestionnaireSummary> getQuestionnaireSumaryReport(Integer questionnaireId);
}
