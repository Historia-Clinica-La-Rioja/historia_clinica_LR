package net.pladema.questionnaires.familybg.getsummary.repository;

import net.pladema.questionnaires.common.dto.QuestionnaireSummary;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import java.util.Optional;

@Repository
public class FamilyBgSummaryRepositoryImpl implements FamilyBgSummaryRepository {

	private final EntityManager entityManager;

	public FamilyBgSummaryRepositoryImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Optional<QuestionnaireSummary> getQuestionnaireSummaryReport(Integer questionnaireId) {
		String query = "SELECT p.first_name, p.middle_names, p.last_name, hp.license_number, to_char(qr.created_on, 'YYYY-MM-DD') " + "FROM  {h-schema}minsal_lr_questionnaire_response qr " + "INNER JOIN {h-schema}healthcare_professional hp ON hp.id = qr.created_by " + "INNER JOIN {h-schema}person p ON p.id = hp.person_id " + "WHERE qr.id = :questionnaireId";

		Optional<Object[]> queryResult = entityManager.createNativeQuery(query).setParameter("questionnaireId", questionnaireId).getResultList().stream().findFirst();

		return queryResult.map(a -> new QuestionnaireSummary((String) a[0], (String) a[1], (String) a[2], (String) a[3], (String) a[4]));
	}
}
