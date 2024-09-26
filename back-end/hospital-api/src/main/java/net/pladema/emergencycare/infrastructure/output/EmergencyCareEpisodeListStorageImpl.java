package net.pladema.emergencycare.infrastructure.output;

import ar.lamansys.sgh.shared.infrastructure.input.service.patient.enums.EPatientType;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import lombok.AllArgsConstructor;
import net.pladema.emergencycare.application.port.output.EmergencyCareEpisodeListStorage;
import net.pladema.emergencycare.domain.EmergencyCareEpisodeFilterBo;
import net.pladema.emergencycare.repository.domain.EmergencyCareVo;
import net.pladema.emergencycare.repository.domain.ProfessionalPersonVo;
import net.pladema.emergencycare.repository.entity.EmergencyCareEpisode;
import net.pladema.emergencycare.service.domain.EmergencyCareBo;

import net.pladema.emergencycare.service.domain.enums.EEmergencyCareState;

import net.pladema.emergencycare.triage.infrastructure.output.entity.Triage;
import net.pladema.emergencycare.triage.repository.entity.TriageCategory;
import net.pladema.establishment.repository.entity.Bed;
import net.pladema.establishment.repository.entity.Room;
import net.pladema.establishment.repository.entity.Sector;
import net.pladema.medicalconsultation.doctorsoffice.repository.entity.DoctorsOffice;
import net.pladema.medicalconsultation.shockroom.infrastructure.repository.entity.Shockroom;
import net.pladema.person.repository.entity.Person;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
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
		String sqlDataWithStatement =
				"WITH last_triage AS (" +
				"      SELECT distinct on (ece.id) ece.id as episodeId, " +
				"									t.id as id, " +
				"									t.clinical_specialty_sector_id as clinicalSpecialtySectorId, " +
				"									t.created_by as creatorId, " +
				"									p.first_name as creatorFirstName, " +
				"									p.last_name as creatorLastName, " +
				"									p.middle_names as creatorMiddleNames, " +
				"									p.other_last_names as creatornOtherLastNames, " +
				"									pe.name_self_determination as creatorNameSelfDetermination" +
				"      FROM emergency_care_episode as ece " +
				"      JOIN triage as t on (ece.id = t.emergency_care_episode_id) " +
				"  	   JOIN user_person as up ON (up.user_id = t.updated_by) " +
				"	   JOIN person as p ON (up.person_id = p.id) " +
				"	   JOIN person_extended as pe ON (p.id = pe.person_id) " +
				"	   ORDER BY ece.id, t.created_on DESC " +
				") ";

		String sqlDataSelectStatement =
				"SELECT ece.id as episodeId, " +
						"ece.patient_id as episodePatientId, " +
						"ece.patient_medical_coverage_id as episodePatientMedicalCoverageId, " +
						"ece.institution_id as episodeIntitutionId, " +
						"ece.emergency_care_type_id as episodeTypeId, " +
						"ece.emergency_care_state_id as episodeStateId, " +
						"ece.emergency_care_entrance_type_id as episodeEntranceTypeId, " +
						"ece.ambulance_company_id as episodeAmbulanceCompanyId, " +
						"ece.created_on as episodeCreatedOn, " +
						"ece.doctors_office_id as episodeDoctorsOffice, " +
						"ece.has_police_intervention as episodehasPoliceIntervention, " +
						"ece.shockroom_id as episodeShockroomId, " +
						"ece.bed_id as episodeBedId, " +
						"ece.reason as episodeReason, " +
						"pe.id as personId, " +
						"pe.first_name as personFirstName, " +
						"pe.last_name as personLastName, " +
						"pe.middle_names as personMiddleNames, " +
						"pe.other_last_names as personOtherLastNames, " +
						"pe.identification_type_id as personidentificationTypeId, " +
						"pe.identification_number as personIdentificationNumber, " +
						"pe.gender_id as personGenderId, " +
						"pe.birth_date as personbirthDate, " +
						"it.description as identificationTypeDescripcion, " +
						"pa.type_id as patientTypeId, " +
						"petd.name_self_determination as personNameSelfDetermination, " +
						"dso.id as doctorsOfficeId, " +
						"dso.description as doctorsOfficeDescription, " +
						"tc.id as triageCategoryId, " +
						"tc.name as triageCategoryName, " +
						"tc.color_code as triageCategoryColorCode, " +
						"s.id as shockroomId, " +
						"s.description as shockroomDescription, " +
						"b.id as bedId, " +
						"b.bed_number as bedNumber, " +
						"b.free as bedFree, " +
						"r.id as roomId, " +
						"r.description as roomDescription, " +
						"r.type as roomType, " +
						"r.room_number as roomNumber, " +
						"se.id as sectorId, " +
						"se.description as sectorDescription, " +
						"se.sector_type_id as sectorTypeId," +
						"lt.id as triageId, " +
						"lt.clinicalSpecialtySectorId as triageClinicalSpecialtySectorId, " +
						"lt.creatorId as triageCreatedBy, " +
						"lt.creatorFirstName as triageCreatorFirstName, " +
						"lt.creatorLastName as triageCreatorLastName, " +
						"lt.creatorMiddleNames as triageCreatorMiddleNames, " +
						"lt.creatorNameSelfDetermination as triageCreatorNameSelfDetermination, " +
						"lt.creatornOtherLastNames as triageCreatorOtherLastName, " +
						"css.description as clinicalSpecialtySectorDescription ";

		String sqlFromStatement =
				"FROM emergency_care_episode AS ece " +
						"LEFT JOIN patient AS pa ON (pa.id = ece.patient_id) " +
						"LEFT JOIN person AS pe ON (pe.id = pa.person_id)" +
						"LEFT JOIN identification_type AS it ON (pe.identification_type_id = it.id) " +
						"LEFT JOIN doctors_office AS dso ON (dso.id = ece.doctors_office_id) " +
						"LEFT JOIN person_extended AS petd ON (pe.id = petd.person_id) " +
						"JOIN triage_category AS tc ON (tc.id = ece.triage_category_id) " +
						"LEFT JOIN shockroom AS s ON (s.id = ece.shockroom_id) " +
						"LEFT JOIN bed AS b ON (ece.bed_id = b.id) " +
						"LEFT JOIN room AS r ON (b.room_id = r.id) " +
						"LEFT JOIN sector AS se ON (se.id = COALESCE(dso.sector_id, s.sector_id, r.sector_id)) " +
						"LEFT JOIN emergency_care_state AS ecs ON (ece.emergency_care_state_id = ecs.id) " +
						"LEFT JOIN last_triage as lt ON (lt.episodeId = ece.id) " +
						"LEFT JOIN clinical_specialty_sector as css on (lt.clinicalSpecialtySectorId = css.id) ";

		String sqlWhereStatement =
				"WHERE (ece.emergency_care_state_id IN (" + STATE_IDS_STRING + "))" +
						"AND ece.institution_id = :institutionId" +
						(filter.getTriageCategoryIds() != null && !filter.getTriageCategoryIds().isEmpty() ? " AND ece.triage_category_id IN :triageCategoryIds" : " ") +
						(filter.getTypeIds() != null && !filter.getTypeIds().isEmpty() ? " AND ece.emergency_care_type_id IN :typeIds": " ") +
						(filter.getStateIds() != null && !filter.getStateIds().isEmpty() ? " AND ecs.id IN :stateIds" : " ") +
						(filter.getPatientId() != null ? " AND pa.id = :patientId" : " ") +
						(filter.getIdentificationNumber() != null ? " AND LOWER(pe.identification_number) LIKE '%" + filter.getIdentificationNumber().toLowerCase() + "%'" : " ") +
						(filter.getPatientFirstName() != null ? featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS) ? " AND ((petd.name_self_determination IS NOT NULL AND LOWER(petd.name_self_determination) LIKE '%" + filter.getPatientFirstName().toLowerCase() + "%') OR (petd.name_self_determination IS NULL AND LOWER(pe.first_name) LIKE '%" + filter.getPatientFirstName().toLowerCase() + "%'))" : " AND LOWER(pe.first_name) LIKE '%" + filter.getPatientFirstName().toLowerCase() + "%'" : " ") +
						(filter.getPatientLastName() != null ? " AND LOWER(pe.last_name) LIKE '%" + filter.getPatientLastName().toLowerCase() + "%'" : " ") +
						(filter.getMustBeEmergencyCareTemporal() != null && filter.getMustBeEmergencyCareTemporal() ? " AND pa.type_id = " + EPatientType.EMERGENCY_CARE_TEMPORARY.getId() : " ") +
						(filter.getClinicalSpecialtySectorIds() != null && !filter.getClinicalSpecialtySectorIds().isEmpty() ? " AND css.id IN :clinicalSpecialtySectorIds " : " ");

		String sqlOrderByStatement = "ORDER BY ecs.order, ece.emergency_care_state_id, ece.triage_category_id, ece.created_on";

		Query resultQuery = entityManager.createNativeQuery(sqlDataWithStatement + sqlDataSelectStatement + sqlFromStatement + sqlWhereStatement + sqlOrderByStatement)
				.setMaxResults(pageable.getPageSize()) //LIMIT
				.setFirstResult(pageable.getPageSize() * pageable.getPageNumber());//OFFSET

		setQueryParameters(filter, institutionId, resultQuery);

		List<EmergencyCareVo> resultVo = createEmergencyCareVoList(resultQuery.getResultList());

		long totalResultAmount = countTotalAmountOfElements(filter, institutionId, sqlDataWithStatement, sqlFromStatement + sqlWhereStatement);
		List<EmergencyCareBo> result = resultVo.stream().map(EmergencyCareBo::new).collect(Collectors.toList());
		return new PageImpl<>(result, pageable, totalResultAmount);
	}

	private long countTotalAmountOfElements(EmergencyCareEpisodeFilterBo filter, Integer institutionId, String withStatement, String fromAndWhereStatement) {
		String sqlCountSelectStatement = "SELECT COUNT(1) ";
		Query resultQuery = entityManager.createNativeQuery(withStatement + sqlCountSelectStatement + fromAndWhereStatement);
		setQueryParameters(filter, institutionId, resultQuery);
		return ((BigInteger) resultQuery.getSingleResult()).longValue();
	}

	private void setQueryParameters(EmergencyCareEpisodeFilterBo filter, Integer institutionId, Query result) {
		result.setParameter("institutionId", institutionId);
		if (filter.getTypeIds()!= null && !filter.getTypeIds().isEmpty())
			result.setParameter("typeIds", filter.getTypeIds());
		if (filter.getTriageCategoryIds()!= null && !filter.getTriageCategoryIds().isEmpty())
			result.setParameter("triageCategoryIds", filter.getTriageCategoryIds());
		if (filter.getStateIds()!= null && !filter.getStateIds().isEmpty())
			result.setParameter("stateIds", filter.getStateIds());
		if (filter.getClinicalSpecialtySectorIds()!= null && !filter.getClinicalSpecialtySectorIds().isEmpty())
			result.setParameter("clinicalSpecialtySectorIds", filter.getClinicalSpecialtySectorIds());
		if (filter.getPatientId() != null)
			result.setParameter("patientId", filter.getPatientId());
	}

	private List<EmergencyCareVo> createEmergencyCareVoList(List<Object[]> rows) {
		return rows.stream().map(row -> {
			var episode = mapToEmergencyCareEpisode(row);
			var person = mapToPerson(row);
			var doctorsOffice = mapToDoctorsOffice(row);
			var triageCategory = mapToTriageCategory(row);
			var shockroom = mapToShockroom(row);
			var bed = mapToBed(row);
			var room = mapToRoom(row);
			var sector = mapToSector(row);
			var personIdentificationType = (String) row[23];
			var patientType = (Short) row[24];
			var personNameSelfDetermination = (String) row[25];
			var triage = mapToTriage(row, episode.getId(), triageCategory.getId(),doctorsOffice.getId());
			var clinicalSpecialtySectorDescription = (String) row[51];
			var triageCreator = (triage.getCreatedBy() != null) ? mapToTriageCreator(row) : null;

			return new EmergencyCareVo(episode,
					person, personIdentificationType, patientType,
					personNameSelfDetermination,
					doctorsOffice, triageCategory,
					shockroom, bed, room, sector,
					triage, clinicalSpecialtySectorDescription, triageCreator);
		}).collect(Collectors.toList());

	}

	private EmergencyCareEpisode mapToEmergencyCareEpisode(Object[] row) {
		var result = new EmergencyCareEpisode();
		result.setId((Integer) row[0]);
		result.setPatientId((Integer) row[1]);
		result.setPatientMedicalCoverageId((Integer) row[2]);
		result.setInstitutionId((Integer) row[3]);
		result.setEmergencyCareTypeId((Short) row[4]);
		result.setEmergencyCareStateId((Short) row[5]);
		result.setEmergencyCareEntranceTypeId((Short) row[6]);
		result.setAmbulanceCompanyId((String) row[7]);
		result.setCreatedOn(((Timestamp) row[8]).toLocalDateTime());
		result.setDoctorsOfficeId((Integer) row[9]);
		result.setHasPoliceIntervention((Boolean) row[10]);
		result.setShockroomId((Integer) row[11]);
		result.setBedId((Integer) row[12]);
		result.setReason((String) row[13]);
		return result;
	}

	private Person mapToPerson(Object[] row) {
		var result = new Person();
		result.setId((Integer) row[14]);
		result.setFirstName((String) row[15]);
		result.setLastName((String) row[16]);
		result.setMiddleNames((String) row[17]);
		result.setOtherLastNames((String) row[18]);
		result.setIdentificationTypeId((Short) row[19]);
		result.setIdentificationNumber((String) row[20]);
		result.setGenderId((Short) row[21]);
		if (row[22] != null)
			result.setBirthDate(((Date) row[22]).toLocalDate());
		return result;
	}

	private DoctorsOffice mapToDoctorsOffice(Object[] row) {
		var result = new DoctorsOffice();
		result.setId((Integer) row[26]);
		result.setDescription((String) row[27]);
		return result;
	}

	private TriageCategory mapToTriageCategory(Object[] row) {
		var result = new TriageCategory();
		result.setId((Short) row[28]);
		result.setName((String) row[29]);
		result.setColorCode((String) row[30]);
		return result;
	}

	private Shockroom mapToShockroom(Object[] row) {
		var result = new Shockroom();
		result.setId((Integer) row[31]);
		result.setDescription((String) row[32]);
		return result;
	}

	private Bed mapToBed(Object[] row) {
		var result = new Bed();
		result.setId((Integer) row[33]);
		result.setBedNumber((String) row[34]);
		result.setFree((Boolean) row[35]);
		return result;
	}

	private Room mapToRoom(Object[] row) {
		var result = new Room();
		result.setId((Integer) row[36]);
		result.setDescription((String) row[37]);
		result.setType((String) row[38]);
		result.setRoomNumber((String) row[39]);
		return result;
	}

	private Sector mapToSector(Object[] row) {
		var result = new Sector();
		result.setId((Integer) row[40]);
		result.setDescription((String) row[41]);
		result.setSectorTypeId((Short) row[42]);
		return result;
	}

	private Triage mapToTriage(Object[] row, Integer emergencyCareEpisodeId, Short triageCategoryId, Integer doctorsOfficeId) {
		var result = new Triage();
		result.setId((Integer) row[43]);
		result.setEmergencyCareEpisodeId(emergencyCareEpisodeId);
		result.setTriageCategoryId(triageCategoryId);
		result.setDoctorsOfficeId(doctorsOfficeId);
		result.setClinicalSpecialtySectorId((Integer) row[44]);
		result.setCreatedBy((Integer) row[45]);
		return result;
	}

	private ProfessionalPersonVo mapToTriageCreator(Object[] row) {
		return new ProfessionalPersonVo(
				(String) row[46],
				(String) row[47],
				(String) row[48],
				(String) row[49],
				(String) row[50]
		);
	}

}