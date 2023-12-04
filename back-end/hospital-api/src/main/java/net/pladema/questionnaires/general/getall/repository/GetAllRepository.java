package net.pladema.questionnaires.general.getall.repository;

import java.util.List;
import java.util.Optional;

import net.pladema.questionnaires.common.dto.QuestionnaireInfo;

public interface GetAllRepository {

	Optional<List<QuestionnaireInfo>> getQuestionnairesInfo(Integer patientId);
}
