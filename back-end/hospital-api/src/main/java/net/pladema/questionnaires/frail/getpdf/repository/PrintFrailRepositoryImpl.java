package net.pladema.questionnaires.frail.getpdf.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import net.pladema.questionnaires.common.domain.Answer;
import net.pladema.questionnaires.common.repository.PrintQuestionnaireRepository;

@Repository
public class PrintFrailRepositoryImpl implements PrintQuestionnaireRepository {

	private final EntityManager entityManager;

	public PrintFrailRepositoryImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public Optional<List<Answer>> getQuestionnaireReportInfo(Long questionnaireTestId) {
		String query = "SELECT ans.item_id, ans.option_id, ans.value " + "FROM {h-schema}minsal_lr_questionnaire_response as qr " + "INNER JOIN {h-schema}minsal_lr_answer ans ON ans.questionnaire_response_id = qr.id " + "WHERE qr.id = :questionnaireTestId";

		List<Object[]> queryResult = entityManager.createNativeQuery(query).setParameter("questionnaireTestId", questionnaireTestId).getResultList();

		List<Answer> answers = new ArrayList<>();

		for (Object[] row : queryResult) {
			Answer answer = new Answer();
			answer.setItemId((Integer) row[0]);
			answer.setAnswerId((Integer) row[1]);
			answer.setValue((String) row[2]);
			answers.add(answer);
		}
		return Optional.of(answers);
	}
}
