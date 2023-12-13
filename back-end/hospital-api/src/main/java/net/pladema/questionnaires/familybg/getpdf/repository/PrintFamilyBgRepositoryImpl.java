package net.pladema.questionnaires.familybg.getpdf.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import net.pladema.questionnaires.common.domain.Answer;

@Repository
public class PrintFamilyBgRepositoryImpl implements PrintFamilyBgRepository {

	private final EntityManager entityManager;

	public PrintFamilyBgRepositoryImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public Optional<List<Answer>> getQuestionnaireReportInfo(Long questionnaireTestId) {
		String query = "SELECT ans.item_id, ans.value, ans.option_id " +
				"FROM {h-schema}minsal_lr_questionnaire_response as qr " +
				"INNER JOIN {h-schema}minsal_lr_answer ans ON ans.questionnaire_response_id = qr.id " +
				"WHERE qr.id = :questionnaireTestId";

		List<Object[]> queryResult = entityManager
				.createNativeQuery(query)
				.setParameter("questionnaireTestId", questionnaireTestId)
				.getResultList();

		List<Answer> answers = new ArrayList<>();

		for (Object[] row : queryResult) {
			Answer answer = new Answer();
			answer.setItemId((Integer) row[0]);
			answer.setValue((String) row[1]);
			answer.setAnswerId((Integer) row[2]);
			answers.add(answer);
		}
		return Optional.of(answers);
	}
}
