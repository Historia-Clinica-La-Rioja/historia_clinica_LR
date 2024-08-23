package net.pladema.patient.repository;

import ar.lamansys.sgh.shared.infrastructure.input.service.patient.enums.EPatientType;
import lombok.AllArgsConstructor;
import net.pladema.clinichistory.hospitalization.repository.domain.InternmentEpisodeStatus;
import net.pladema.emergencycare.repository.entity.EmergencyCareState;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.patient.controller.dto.PatientSearchFilter;
import net.pladema.patient.repository.entity.Patient;
import net.pladema.patient.repository.entity.PatientType;
import net.pladema.patient.service.domain.PatientSearch;
import net.pladema.person.repository.entity.Person;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.math.BigInteger;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Repository
public class PatientRepositoryImpl implements PatientRepositoryCustom {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<PatientSearch> getAllByOptionalFilter(PatientSearchFilter searchFilter, Integer resultSize) {
		String queryString = "SELECT p.id as patientId, p2.id as personId, p2.first_name, p2.middle_names, p2.last_name, " +
				"p2.other_last_names, p2.gender_id, p2.identification_type_id, p2.identification_number, p2.birth_date, pt.active, " +
				"pe.name_self_determination " +
				"FROM {h-schema}patient p " +
				"JOIN {h-schema}person p2 ON (p.person_id = p2.id) " +
				"JOIN {h-schema}patient_type pt ON (p.type_id = pt.id) " +
				"LEFT JOIN {h-schema}person_extended pe ON (pe.person_id = p2.id) " +
				"WHERE p.deleted = FALSE " +
				"AND p.type_id != " + EPatientType.REJECTED.getId() + " " +
				(searchFilter.getFirstName() != null && !searchFilter.getFirstName().isBlank() ? (searchFilter.getFilterByNameSelfDetermination() ? "AND ((pe.name_self_determination IS NOT NULL AND UPPER(pe.name_self_determination) LIKE '%" + searchFilter.getFirstName().toUpperCase() + "%') OR (pe.name_self_determination IS NULL AND UPPER(p2.first_name) LIKE '%" + searchFilter.getFirstName().toUpperCase() + "%')) " : "AND UPPER(p2.first_name) LIKE '%" + searchFilter.getFirstName().toUpperCase() + "%' ") : "") +
				(searchFilter.getMiddleNames() != null && !searchFilter.getMiddleNames().isBlank() ? "AND UPPER(p2.middle_names) LIKE '%" + searchFilter.getMiddleNames().toUpperCase() + "%' " : "") +
				(searchFilter.getLastName() != null && !searchFilter.getLastName().isBlank() ? "AND UPPER(p2.last_name) LIKE '%" + searchFilter.getLastName().toUpperCase() + "%' " : "") +
				(searchFilter.getOtherLastNames() != null ? "AND UPPER(p2.other_last_names) LIKE '%" + searchFilter.getOtherLastNames().toUpperCase() +"%' " : "") +
				(searchFilter.getGenderId() != null ? "AND p2.gender_id = " + searchFilter.getGenderId() + " " : "") +
				(searchFilter.getIdentificationNumber() != null && !searchFilter.getIdentificationNumber().isBlank() ? "AND p2.identification_number = '" + searchFilter.getIdentificationNumber() + "' " : "") +
				(searchFilter.getIdentificationTypeId() != null ? "AND p2.identification_type_id = " + searchFilter.getIdentificationTypeId() + " " : "") +
				(searchFilter.getBirthDate() != null ? "AND p2.birth_date = '" + searchFilter.getBirthDate() + "' " : "");

		Query query = entityManager.createNativeQuery(queryString);
		query.setMaxResults(resultSize);

        return parseToPatientSearchList(query.getResultList());
    }

	private List<PatientSearch> parseToPatientSearchList(List<Object[]> queryResult) {
		return queryResult.stream().map(this::parseToPatientSearch).collect(Collectors.toList());
	}

	private PatientSearch parseToPatientSearch(Object[] data) {
		return new PatientSearch(mapToPersonEntity(data), (Integer) data[0], (Boolean) data[10], 0, (String) data[11]);
	}

	private Person mapToPersonEntity(Object[] data) {
		Person result = new Person();
		result.setId((Integer) data[1]);
		result.setFirstName((String) data[2]);
		result.setMiddleNames((String) data[3]);
		result.setLastName((String) data[4]);
		result.setOtherLastNames((String) data[5]);
		result.setGenderId((Short) data[6]);
		result.setIdentificationTypeId((Short) data[7]);
		result.setIdentificationNumber((String) data[8]);
		result.setBirthDate(data[9] != null ? ((Date) data[9]).toLocalDate() : null);
		return result;
	}

	public Integer getCountByOptionalFilter(PatientSearchFilter searchFilter) {
		String queryString = "SELECT COUNT(DISTINCT p.id) " +
				"FROM {h-schema} patient p " +
				"JOIN {h-schema}person p2 ON (p.person_id = p2.id) " +
				"JOIN {h-schema}patient_type pt ON (p.type_id = pt.id) " +
				"LEFT JOIN {h-schema}person_extended pe ON (pe.person_id = p2.id) " +
				"WHERE p.deleted = FALSE " +
				(searchFilter.getFirstName() != null && !searchFilter.getFirstName().isBlank() ? (searchFilter.getFilterByNameSelfDetermination() ? "AND ((pe.name_self_determination IS NOT NULL AND UPPER(pe.name_self_determination) LIKE '%" + searchFilter.getFirstName().toUpperCase() + "%') OR (pe.name_self_determination IS NULL AND UPPER(p2.first_name) LIKE '%" + searchFilter.getFirstName().toUpperCase() + "%')) " : "AND UPPER(p2.first_name) LIKE '%" + searchFilter.getFirstName().toUpperCase() + "%' ") : "") +
				(searchFilter.getMiddleNames() != null && !searchFilter.getMiddleNames().isBlank() ? "AND UPPER(p2.middle_names) LIKE '%" + searchFilter.getMiddleNames().toUpperCase() + "%' " : "") +
				(searchFilter.getLastName() != null && !searchFilter.getLastName().isBlank() ? "AND UPPER(p2.last_name) LIKE '%" + searchFilter.getLastName().toUpperCase() + "%' " : "") +
				(searchFilter.getOtherLastNames() != null ? "AND UPPER(p2.other_last_names) LIKE '%" + searchFilter.getOtherLastNames().toUpperCase() +"%' " : "") +
				(searchFilter.getGenderId() != null ? "AND p2.gender_id = " + searchFilter.getGenderId() + " " : "") +
				(searchFilter.getIdentificationNumber() != null && !searchFilter.getIdentificationNumber().isBlank() ? "AND p2.identification_number = '" + searchFilter.getIdentificationNumber() + "' " : "") +
				(searchFilter.getIdentificationTypeId() != null ? "AND p2.identification_type_id = " + searchFilter.getIdentificationTypeId() + " " : "") +
				(searchFilter.getBirthDate() != null ? "AND p2.birth_date = '" + searchFilter.getBirthDate() + "' " : "");

        Query query = entityManager.createNativeQuery(queryString);

		BigInteger result = (BigInteger) query.getSingleResult();
		return result.intValue();
    }

	@Override
	public List<Patient> getLongTermTemporaryPatientIds(LocalDateTime maxDate, Short limit) {
		String sqlQuery = "SELECT p " +
				"FROM Patient p " +
				"WHERE p.typeId = :typeId " +
				"AND p.deleteable.deleted = false " +
				"AND p.id IN (" +
				"SELECT DISTINCT ph.patientId " +
				"FROM PatientHistory ph " +
				"WHERE ph.typeId = 3 AND ph.createdOn <= :maxDate) " +
				"AND NOT EXISTS (SELECT 1 " +
				"FROM InternmentEpisode ie " +
				"WHERE ie.statusId = :activeStatusId " +
				"AND ie.patientId = p.id ) " +
				"AND NOT EXISTS (SELECT 1 " +
				"FROM EmergencyCareEpisode ece " +
				"WHERE ece.emergencyCareStateId in (:emergencyCareStateIds) " +
				"AND ece.patientId = p.id ) " +
				"AND NOT EXISTS (SELECT 1 " +
				"FROM Appointment a " +
				"WHERE a.appointmentStateId NOT IN (:appointmentStatsIds) " +
				"AND a.patientId = p.id )";
		List<Patient> result = entityManager.createQuery(sqlQuery)
				.setParameter("typeId", PatientType.TEMPORARY)
				.setParameter("maxDate", maxDate)
				.setParameter("activeStatusId", InternmentEpisodeStatus.ACTIVE_ID)
				.setParameter("emergencyCareStateIds", List.of(EmergencyCareState.EN_ATENCION, EmergencyCareState.EN_ESPERA, EmergencyCareState.CON_ALTA_MEDICA))
				.setParameter("appointmentStatsIds", List.of(AppointmentState.CANCELLED, AppointmentState.SERVED))
				.setMaxResults(limit)
				.getResultList();
		return result;
	}
}