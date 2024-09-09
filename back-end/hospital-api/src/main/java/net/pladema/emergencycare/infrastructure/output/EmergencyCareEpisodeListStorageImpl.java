package net.pladema.emergencycare.infrastructure.output;

import ar.lamansys.sgh.shared.infrastructure.input.service.patient.enums.EPatientType;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import lombok.AllArgsConstructor;
import net.pladema.emergencycare.application.port.output.EmergencyCareEpisodeListStorage;
import net.pladema.emergencycare.domain.EmergencyCareEpisodeFilterBo;
import net.pladema.emergencycare.repository.domain.EmergencyCareVo;
import net.pladema.emergencycare.service.domain.EmergencyCareBo;

import net.pladema.emergencycare.service.domain.enums.EEmergencyCareState;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Repository
public class EmergencyCareEpisodeListStorageImpl implements EmergencyCareEpisodeListStorage {

	private EntityManager entityManager;

	private FeatureFlagsService featureFlagsService;

	private final String STATE_IDS_STRING = EEmergencyCareState.getAllForEmergencyCareList()
			.stream().map(String::valueOf)
			.collect(Collectors.joining(", "));

	@Override
	public Page<EmergencyCareBo> getAllEpisodeListByFilter(Integer institutionId, EmergencyCareEpisodeFilterBo filter, Pageable pageable) {
		String sqlDataSelectStatement =
				"SELECT NEW net.pladema.emergencycare.repository.domain.EmergencyCareVo(ece, pe, it.description, pa.typeId, petd.nameSelfDetermination, " +
						"dso, tc, s, b, r, se) ";

		String sqlFromStatement =
				"FROM EmergencyCareEpisode ece " +
						"LEFT JOIN Patient pa ON (pa.id = ece.patientId) " +
						"LEFT JOIN Person pe ON (pe.id = pa.personId)" +
						"LEFT JOIN IdentificationType it ON (pe.identificationTypeId = it.id) " +
						"LEFT JOIN DoctorsOffice dso ON (dso.id = ece.doctorsOfficeId) " +
						"LEFT JOIN PersonExtended petd ON (pe.id = petd.id) " +
						"JOIN TriageCategory tc ON (tc.id = ece.triageCategoryId) " +
						"LEFT JOIN Shockroom s ON (s.id = ece.shockroomId) " +
						"LEFT JOIN Bed b ON (ece.bedId = b.id) " +
						"LEFT JOIN Room r ON (b.roomId = r.id) " +
						"LEFT JOIN Sector se ON (se.id = COALESCE(dso.sectorId, s.sectorId, r.sectorId)) " +
						"LEFT JOIN EmergencyCareState ecs ON (ece.emergencyCareStateId = ecs.id)";

		String sqlWhereStatement =
				"WHERE (ece.emergencyCareStateId IN (" + STATE_IDS_STRING + "))" +
						"AND ece.institutionId = :institutionId" +
						(filter.getTriageCategoryIds() != null && !filter.getTriageCategoryIds().isEmpty() ? " AND ece.triageCategoryId IN :triageCategoryIds" : " ") +
						(filter.getTypeIds() != null && !filter.getTypeIds().isEmpty() ? " AND ece.emergencyCareTypeId IN :typeIds": " ") +
						(filter.getStateIds() != null && !filter.getStateIds().isEmpty() ? " AND ecs.id IN :stateIds" : " ") +
						(filter.getPatientId() != null ? " AND pa.id = :patientId" : " ") +
						(filter.getIdentificationNumber() != null ? " AND LOWER(pe.identificationNumber) LIKE '%" + filter.getIdentificationNumber().toLowerCase() + "%'" : " ") +
						(filter.getPatientFirstName() != null ? featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS) ? " AND ((petd.nameSelfDetermination IS NOT NULL AND LOWER(petd.nameSelfDetermination) LIKE '%" + filter.getPatientFirstName().toLowerCase() + "%') OR (petd.nameSelfDetermination IS NULL AND LOWER(pe.firstName) LIKE '%" + filter.getPatientFirstName().toLowerCase() + "%'))" : " AND LOWER(pe.firstName) LIKE '%" + filter.getPatientFirstName().toLowerCase() + "%'" : " ") +
						(filter.getPatientLastName() != null ? " AND LOWER(pe.lastName) LIKE '%" + filter.getPatientLastName().toLowerCase() + "%'" : " ") +
						(filter.getMustBeEmergencyCareTemporal() != null && filter.getMustBeEmergencyCareTemporal() ? " AND pa.typeId = " + EPatientType.EMERGENCY_CARE_TEMPORARY.getId() : " ");


		String sqlOrderByStatement = "ORDER BY ecs.order, ece.emergencyCareStateId, ece.triageCategoryId, ece.creationable.createdOn";

		Query resultQuery = entityManager.createQuery(sqlDataSelectStatement + sqlFromStatement + sqlWhereStatement + sqlOrderByStatement)
				.setMaxResults(pageable.getPageSize()) //LIMIT
				.setFirstResult(pageable.getPageSize() * pageable.getPageNumber());//OFFSET

		setQueryParameters(filter, institutionId, resultQuery);

		List<EmergencyCareVo> resultData = resultQuery.getResultList();

		long totalResultAmount = countTotalAmountOfElements(filter, institutionId, sqlFromStatement + sqlWhereStatement);
		List<EmergencyCareBo> result = resultData.stream().map(EmergencyCareBo::new).collect(Collectors.toList());
		return new PageImpl<>(result, pageable, totalResultAmount);
	}

	private long countTotalAmountOfElements(EmergencyCareEpisodeFilterBo filter, Integer institutionId, String fromAndWhereStatement) {
		String sqlCountSelectStatement = "SELECT COUNT(1) ";
		Query resultQuery = entityManager.createQuery(sqlCountSelectStatement + fromAndWhereStatement);
		setQueryParameters(filter, institutionId, resultQuery);
		return (long) resultQuery.getSingleResult();
	}

	private void setQueryParameters(EmergencyCareEpisodeFilterBo filter, Integer institutionId, Query result) {
		result.setParameter("institutionId", institutionId);
		if (filter.getTypeIds()!= null && !filter.getTypeIds().isEmpty())
			result.setParameter("typeIds", filter.getTypeIds());
		if (filter.getTriageCategoryIds()!= null && !filter.getTriageCategoryIds().isEmpty())
			result.setParameter("triageCategoryIds", filter.getTriageCategoryIds());
		if (filter.getStateIds()!= null && !filter.getStateIds().isEmpty())
			result.setParameter("stateIds", filter.getStateIds());
		if (filter.getPatientId() != null)
			result.setParameter("patientId", filter.getPatientId());
	}

}