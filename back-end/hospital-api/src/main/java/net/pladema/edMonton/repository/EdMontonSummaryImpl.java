package net.pladema.edMonton.repository;

import net.pladema.edMonton.get.controller.dto.EdMontonSummary;

import org.checkerframework.checker.nullness.Opt;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class EdMontonSummaryImpl implements EdMontonSummaryRepository{

	private final EntityManager entityManager;

	public EdMontonSummaryImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public Optional<EdMontonSummary> getSummaryReportEdMonton(Integer edMontonId) {


		String sql = "SELECT p.first_name, p.middle_names, p.last_name, hp.license_number, to_char(qr.created_on, 'YYYY-MM-DD') " +
				"FROM  {h-schema}minsal_lr_questionnaire_response qr "
				+ "INNER JOIN {h-schema}healthcare_professional hp ON hp.id = qr.created_by "
				+ "INNER JOIN {h-schema}person p ON p.id = hp.person_id "
				+ "WHERE qr.id = :edMontonId";

		Optional<Object[]> queryResult = entityManager.createNativeQuery(sql)
				.setParameter("edMontonId", edMontonId)
				.getResultList().stream().findFirst();

		Optional<EdMontonSummary> result = queryResult.map(a -> new EdMontonSummary(
				(String) a[0],
				(String) a[1],
				(String) a[2],
				(String) a[3],
				(String) a[4]

		));
		return result;
	}
}
