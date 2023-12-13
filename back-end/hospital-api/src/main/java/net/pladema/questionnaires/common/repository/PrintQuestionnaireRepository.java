package net.pladema.questionnaires.common.repository;

import java.util.List;
import java.util.Optional;

import net.pladema.questionnaires.common.domain.Answer;

public interface PrintQuestionnaireRepository {

	Optional<List<Answer>> getQuestionnaireReportInfo(Long questionnaireTestId);
}
