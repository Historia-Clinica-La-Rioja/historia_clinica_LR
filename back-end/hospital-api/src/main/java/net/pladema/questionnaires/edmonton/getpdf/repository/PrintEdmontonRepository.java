package net.pladema.questionnaires.edmonton.getpdf.repository;

import java.util.List;
import java.util.Optional;

import net.pladema.questionnaires.common.domain.Answer;

public interface PrintEdmontonRepository {

	Optional<List<Answer>> getQuestionnaireReportInfo(Long questionnaireTestId);
}
