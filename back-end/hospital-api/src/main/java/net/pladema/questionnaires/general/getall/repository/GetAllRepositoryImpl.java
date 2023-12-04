package net.pladema.questionnaires.general.getall.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import net.pladema.questionnaires.common.dto.QuestionnaireInfo;

@Repository
public class GetAllRepositoryImpl implements GetAllRepository {
	private final EntityManager entityManager;

	public GetAllRepositoryImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public Optional<List<QuestionnaireInfo>> getQuestionnairesInfo(Integer patientId) {
		String query = "SELECT qr.id, qr_st.description, qr.created_on, CONCAT(pe.last)";
		List<Object[]> queryResult = entityManager.createNativeQuery(query).setParameter("patientId", patientId).getResultList();

		List<QuestionnaireInfo> questionnairesInfo = new ArrayList<>();

		for (Object[] row : queryResult) {
			QuestionnaireInfo questionnaireInfo = new QuestionnaireInfo();
			questionnaireInfo.setQuestionnaireId(String.valueOf(row[0]));
			questionnaireInfo.setInstitution();
		}
		return Optional.of(questionnairesInfo);
	}
}
