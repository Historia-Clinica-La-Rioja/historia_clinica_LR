package net.pladema.questionnaires.frail.getpdf.repository;

import java.util.List;
import java.util.Optional;

import net.pladema.questionnaires.common.domain.Answer;

public interface PrintFrailRepository {

	Optional<List<Answer>> getQuestionnaireReportInfo(Long questionnaireTestId);
}
