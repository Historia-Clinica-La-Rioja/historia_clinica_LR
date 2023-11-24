package net.pladema.questionnaires.familybg.getpdf.repository;

import net.pladema.questionnaires.common.domain.Answer;

import java.util.List;
import java.util.Optional;

public interface PrintFamilyBgRepository {

	Optional<List<Answer>> getQuestionnaireReportInfo(Long questionnaireTestId);
}
