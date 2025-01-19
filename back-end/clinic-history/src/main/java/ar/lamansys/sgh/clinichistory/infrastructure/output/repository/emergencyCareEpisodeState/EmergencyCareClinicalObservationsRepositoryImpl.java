package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.emergencyCareEpisodeState;

import ar.lamansys.sgh.clinichistory.domain.ips.MapClinicalObservationVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ClinicalObservationVo;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EmergencyCareClinicalObservationsRepositoryImpl implements EmergencyCareClinicalObservationsRepository {

	private static final String SELECT = "SELECT ovs.id, s.sctid, ovs.status_id, ovs.value, ovs.effective_time ";
	private static final String WHERE = "WHERE source_id = :episodeId " +
										"AND source_type_id = " + SourceType.EMERGENCY_CARE +" "+
										"AND d.status_id = :statusId ";

	private final EntityManager entityManager;

	public EmergencyCareClinicalObservationsRepositoryImpl(EntityManager entityManager){
		super();
		this.entityManager = entityManager;
	}

	@Transactional(readOnly = true)
	@Override
	public MapClinicalObservationVo getGeneralState(Integer episodeId) {
		String sqlQuery = SELECT
						+ getDocumentVitalSignFromStatement()
						+ WHERE
						+ " UNION "
						+ SELECT
						+ getDocumentLabFromStatement()
						+ WHERE;
		return executeQuery(sqlQuery, episodeId);
	}

	private String getDocumentVitalSignFromStatement() {
		return 	"   FROM {h-schema}document d " +
				"   JOIN {h-schema}document_vital_sign dvs ON (d.id = dvs.document_id) " +
				"   JOIN {h-schema}observation_vital_sign ovs ON (dvs.observation_vital_sign_id = ovs.id) " +
				"   JOIN {h-schema}snomed s ON (ovs.snomed_id = s.id) ";
	}

	private String getDocumentLabFromStatement() {
		return "	FROM {h-schema}document d " +
				"   JOIN {h-schema}document_lab dl ON (d.id = dl.document_id) " +
				"   JOIN {h-schema}observation_lab ovs ON (dl.observation_lab_id = ovs.id) " +
				"   JOIN {h-schema}snomed s ON (ovs.snomed_id = s.id) ";
	}

	private MapClinicalObservationVo executeQuery(String sqlQuery, Integer episodeId) {
		Query query = entityManager.createNativeQuery(sqlQuery);
		query.setParameter("episodeId", episodeId);
		query.setParameter("statusId", DocumentStatus.FINAL);
		List<Object[]> queryResult = query.getResultList();
		List<ClinicalObservationVo> clinicalObservationVos = new ArrayList<>();
		queryResult.forEach(ovs -> {
			Timestamp timeStamp = (Timestamp) ovs[4];
			clinicalObservationVos.add(new ClinicalObservationVo((int) ovs[0], (String) ovs[1], (String) ovs[2], (String) ovs[3], timeStamp != null ? timeStamp.toLocalDateTime() : null));
		});
		MapClinicalObservationVo result = new MapClinicalObservationVo(clinicalObservationVos);
		return result;
	}
}
