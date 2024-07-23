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

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Repository
public class EmergencyCareEpisodeListStorageImpl implements EmergencyCareEpisodeListStorage {

	private EntityManager entityManager;

	private FeatureFlagsService featureFlagsService;

	private final Short undefinedEmergencyCareType = -1;

	private final String STATE_IDS_STRING = EEmergencyCareState.getAllForEmergencyCareList()
			.stream().map(String::valueOf)
			.collect(Collectors.joining(", "));

	@Override
	public Page<EmergencyCareBo> getAllEpisodeListByFilter(Integer institutionId, EmergencyCareEpisodeFilterBo filter, Pageable pageable) {
		String sqlDataSelectStatement =
				"SELECT NEW net.pladema.emergencycare.repository.domain.EmergencyCareVo(ece, pe, pa.typeId, petd.nameSelfDetermination, " +
						"dso.description, tc, s.description, b) ";

		String sqlFromStatement =
				"FROM EmergencyCareEpisode ece " +
						"LEFT JOIN Patient pa ON (pa.id = ece.patientId) " +
						"LEFT JOIN Person pe ON (pe.id = pa.personId) " +
						"LEFT JOIN DoctorsOffice dso ON (dso.id = ece.doctorsOfficeId) " +
						"LEFT JOIN PersonExtended petd ON (pe.id = petd.id) " +
						"JOIN TriageCategory tc ON (tc.id = ece.triageCategoryId) " +
						"LEFT JOIN Shockroom s ON (s.id = ece.shockroomId) " +
						"LEFT JOIN Bed b ON (ece.bedId = b.id) ";

		String sqlWhereStatement =
				"WHERE (ece.emergencyCareStateId IN (" + STATE_IDS_STRING + "))" +
						"AND ece.institutionId = " + institutionId +
						(filter.getTriageCategoryId() != null ? " AND ece.triageCategoryId = " + filter.getTriageCategoryId() : " ") +
						(filter.getPatientId() != null ? " AND pa.id = " + filter.getPatientId() : " ") +
						(filter.getIdentificationNumber() != null ? " AND LOWER(pe.identificationNumber) LIKE '%" + filter.getIdentificationNumber().toLowerCase() + "%'" : " ") +
						(filter.getPatientFirstName() != null ? featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS) ? " AND ((petd.nameSelfDetermination IS NOT NULL AND LOWER(petd.nameSelfDetermination) LIKE '%" + filter.getPatientFirstName().toLowerCase() + "%') OR (petd.nameSelfDetermination IS NULL AND LOWER(pe.firstName) LIKE '%" + filter.getPatientFirstName().toLowerCase() + "%'))" : " AND LOWER(pe.firstName) LIKE '%" + filter.getPatientFirstName().toLowerCase() + "%'" : " ") +
						(filter.getPatientLastName() != null ? " AND LOWER(pe.lastName) LIKE '%" + filter.getPatientLastName().toLowerCase() + "%'" : " ") +
						(filter.getMustBeTemporal() != null && filter.getMustBeTemporal() ? " AND pa.typeId = " + EPatientType.TEMPORARY.getId() : " ") +
						(filter.getMustBeEmergencyCareTemporal() != null && filter.getMustBeEmergencyCareTemporal() ? " AND pa.typeId = " + EPatientType.EMERGENCY_CARE_TEMPORARY.getId() : " ");


		if (filter.getTypeId() != null) {
			sqlWhereStatement += filter.getTypeId().equals(undefinedEmergencyCareType) ? " AND ece.emergencyCareTypeId is NULL " : " AND ece.emergencyCareTypeId = " + filter.getTypeId() + " ";
		}

		String sqlOrderByStatement = "ORDER BY ece.emergencyCareStateId, ece.triageCategoryId, ece.creationable.createdOn";

		List<EmergencyCareVo> resultData = entityManager.createQuery(sqlDataSelectStatement + sqlFromStatement + sqlWhereStatement + sqlOrderByStatement)
				.setMaxResults(pageable.getPageSize()) //LIMIT
				.setFirstResult(pageable.getPageSize() * pageable.getPageNumber()) //OFFSET
				.getResultList();

		long totalResultAmount = countTotalAmountOfElements(sqlFromStatement + sqlWhereStatement);
		List<EmergencyCareBo> result = resultData.stream().map(EmergencyCareBo::new).collect(Collectors.toList());
		return new PageImpl<>(result, pageable, totalResultAmount);
	}

	private long countTotalAmountOfElements(String fromAndWhereStatement) {
		String sqlCountSelectStatement = "SELECT COUNT(1) ";
		return (long) entityManager.createQuery(sqlCountSelectStatement + fromAndWhereStatement).getSingleResult();
	}

}