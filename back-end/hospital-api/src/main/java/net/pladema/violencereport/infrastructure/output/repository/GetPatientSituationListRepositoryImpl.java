package net.pladema.violencereport.infrastructure.output.repository;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.violencereport.domain.ViolenceReportSituationBo;

@Slf4j
@AllArgsConstructor
@Repository
public class GetPatientSituationListRepositoryImpl implements GetPatientSituationListRepository {

	private EntityManager entityManager;

	@Override
	public Page<ViolenceReportSituationBo> getPatientSituations(Integer patientId, Boolean mustBeLimited, Pageable pageable) {
		log.debug("Input parameters -> patientId {}, mustBeLimited {}, pageable {}", patientId, mustBeLimited, pageable);
		final int MAX_LIMITED_RESULTS = 5;
		String querySelectString = "SELECT vr.situation_id, risk_level_id, fsd.first_date, vr.created_on ";
		String queryCountSelectString = "SELECT COUNT(1) ";

		String queryWithString =
				"WITH last_patient_situation_evolution AS (" +
					"SELECT vr.situation_id, vr.patient_id , MAX(vr.evolution_id) AS last_evolution_id " +
					"FROM {h-schema}violence_report vr " +
					"GROUP BY vr.situation_id, vr.patient_id)," +
				"first_situation_date AS (" +
					"SELECT vr.situation_id , vr.patient_id , MIN(vr.created_on) AS first_date " +
					"FROM {h-schema}violence_report vr " +
					"GROUP BY vr.situation_id , vr.patient_id)";

		String queryFromString = "FROM {h-schema}violence_report vr " +
				"JOIN last_patient_situation_evolution lpse ON (lpse.situation_id = vr.situation_id AND lpse.patient_id = vr.patient_id AND lpse.last_evolution_id = vr.evolution_id) " +
				"JOIN first_situation_date fsd ON (fsd.situation_id = vr.situation_id AND fsd.patient_id = vr.patient_id) " +
				"WHERE vr.patient_id = :patientId";

		String queryOrderString = " ORDER BY vr.situation_id";

		String queryString = queryWithString + querySelectString + queryFromString + queryOrderString;
		Query query = entityManager.createNativeQuery(queryString).setParameter("patientId", patientId);
		if (mustBeLimited)
			query.setMaxResults(MAX_LIMITED_RESULTS);
		List<?> rows = query.getResultList();
		List<ViolenceReportSituationBo> situations = rows.stream().map(row -> parseViolenceSituationBo((Object[]) row)).collect(Collectors.toList());
		Integer getAllElements = getAllElementsAmount(patientId, queryWithString + queryCountSelectString + queryFromString);
		Page<ViolenceReportSituationBo> result = new PageImpl<>(situations, pageable, getAllElements);
		log.debug("Output -> {}", result);
		return result;
	}

	private Integer getAllElementsAmount(Integer patientId, String queryString) {
		BigInteger result = (BigInteger) entityManager.createNativeQuery(queryString).setParameter("patientId", patientId).getSingleResult();
		return result.intValue();
	}

	private ViolenceReportSituationBo parseViolenceSituationBo(Object[] row) {
		ViolenceReportSituationBo violenceReportSituation = new ViolenceReportSituationBo();
		violenceReportSituation.setSituationId((Short) row[0]);
		violenceReportSituation.setRiskLevelId((Short) row[1]);
		violenceReportSituation.setInitialDate(((Timestamp) row[2]).toLocalDateTime().toLocalDate());
		violenceReportSituation.setLastModificationDate(((Timestamp) row[3]).toLocalDateTime().toLocalDate());
		return violenceReportSituation;
	}

}
