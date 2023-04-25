package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.emergencyCareEpisodeState;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.HealthConditionVo;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.Snomed;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionVerificationStatus;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.math.BigInteger;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EmergencyCareEpisodeHealthConditionRepositoryImpl implements EmergencyCareEpisodeHealthConditionRepository{

	private final EntityManager entityManager;

	public EmergencyCareEpisodeHealthConditionRepositoryImpl(EntityManager entityManager){
		super();
		this.entityManager = entityManager;
	}

	@Override
	@Transactional(readOnly = true)
	public List<HealthConditionVo> findAllDiagnoses(Integer episodeId) {
		String sqlString = "WITH t AS (" +
				"SELECT hc.id, snomed_id, hc.status_id, hc.main, verification_status_id, problem_id, start_date, hc.note_id, hc.updated_on, " +
				"row_number() OVER (PARTITION BY snomed_id, problem_id order by hc.updated_on DESC) AS rw " +
				"FROM {h-schema}document d " +
				"JOIN {h-schema}document_health_condition dhc ON (d.id = dhc.document_id) " +
				"JOIN {h-schema}health_condition hc ON (dhc.health_condition_id = hc.id) " +
				"WHERE d.source_id = :episodeId " +
				"AND d.source_type_id = " + SourceType.EMERGENCY_CARE +" "+
				"AND d.status_id IN (:statusId) )" +
				"SELECT t.id AS id, s.sctid AS sctid, s.pt, status_id, t.main, verification_status_id, problem_id, " +
				"start_date, n.id AS note_id, n.description AS note " +
				"FROM t " +
				"LEFT JOIN {h-schema}note n ON (note_id = n.id) " +
				"JOIN {h-schema}snomed s ON (snomed_id = s.id) " +
				"WHERE rw = 1 AND NOT verification_status_id = :verificationId " +
				"ORDER BY t.updated_on DESC";

		List<Object[]> queryResult = entityManager.createNativeQuery(sqlString)
				.setParameter("statusId", List.of(DocumentStatus.FINAL, DocumentStatus.DRAFT))
				.setParameter("verificationId", ConditionVerificationStatus.ERROR)
				.setParameter("episodeId", episodeId)
				.getResultList();

		List<HealthConditionVo> result = new ArrayList<>();

		queryResult.forEach(h ->
				result.add(new HealthConditionVo((Integer)h[0],
						new Snomed((String)h[1], (String)h[2], null, null),
						(String)h[3],
						(boolean)h[4],
						(String)h[5],
						(String)h[6],
						h[7] != null ? ((Date)h[7]).toLocalDate() : null,
						h[8] != null ? ((BigInteger)h[8]).longValue() : null,
						(String)h[9]))
		);
		return result;
	}

}
