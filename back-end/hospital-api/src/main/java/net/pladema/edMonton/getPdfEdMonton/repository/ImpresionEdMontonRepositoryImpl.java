package net.pladema.edMonton.getPdfEdMonton.repository;

import net.pladema.edMonton.repository.domain.Answer;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ImpresionEdMontonRepositoryImpl implements ImpresionEdMontonRepository{

	private final EntityManager entityManager;

	public ImpresionEdMontonRepositoryImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public Optional<List<Answer>> getEdMontonReportInfo(Long edMontonTestId) {

		String query = "SELECT ans.item_id, ans.option_id " +
				"FROM {h-schema}minsal_lr_questionnaire_response as qr " +
				"INNER JOIN {h-schema}minsal_lr_answer ans ON ans.questionnaire_response_id = qr.id " +
				"WHERE qr.id = :edMontonTestId";
		List<Object[]> queryResult = entityManager.createNativeQuery(query)
				.setParameter("edMontonTestId", edMontonTestId)
				.getResultList();

		List<Answer> answers = new ArrayList<>();
		for(Object[] row : queryResult){
			Answer answer = new Answer();
			answer.setItemId((Integer)row[0]);
			answer.setAnswerId((Integer) row[1]);
			answers.add(answer);
		}
		return Optional.of(answers);
	}
}
