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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Repository
public class PatientRepositoryImpl implements PatientRepositoryCustom {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Page<PatientSearch> getAllByOptionalFilter(PatientSearchFilter searchFilter, Pageable pageable) {
		String fromStatement = "FROM Patient p " +
				"JOIN Person p2 ON (p2.id = p.personId) " +
				"JOIN PatientType pt ON (pt.id = p.typeId) " +
				"LEFT JOIN PersonExtended pe ON (pe.id = p2.id) ";

		String whereStatement =	"WHERE p.deleteable.deleted = FALSE " +
				"AND p.typeId != " + EPatientType.REJECTED.getId() + " " +
				(searchFilter.getFirstName() != null && !searchFilter.getFirstName().isBlank() ? ((searchFilter.getFilterByNameSelfDetermination() ? "AND UPPER(COALESCE(pe.nameSelfDetermination, p2.firstName)) " : "AND UPPER(p2.firstName) ") + "LIKE '" + searchFilter.getFirstName().toUpperCase() + "%' ") : "") +
				(searchFilter.getMiddleNames() != null && !searchFilter.getMiddleNames().isBlank() ? "AND UPPER(p2.middleNames) LIKE '" + searchFilter.getMiddleNames().toUpperCase() + "%' " : "") +
				(searchFilter.getLastName() != null && !searchFilter.getLastName().isBlank() ? "AND UPPER(p2.lastName) LIKE '" + searchFilter.getLastName().toUpperCase() + "%' " : "") +
				(searchFilter.getOtherLastNames() != null ? "AND UPPER(p2.otherLastNames) LIKE '" + searchFilter.getOtherLastNames().toUpperCase() +"%' " : "") +
				(searchFilter.getGenderId() != null ? "AND p2.genderId = " + searchFilter.getGenderId() + " " : "") +
				(searchFilter.getIdentificationNumber() != null && !searchFilter.getIdentificationNumber().isBlank() ? "AND p2.identificationNumber = '" + searchFilter.getIdentificationNumber() + "' " : "") +
				(searchFilter.getIdentificationTypeId() != null ? "AND p2.identificationTypeId = " + searchFilter.getIdentificationTypeId() + " " : "") +
				(searchFilter.getBirthDate() != null ? "AND p2.birthDate = '" + searchFilter.getBirthDate() + "' " : "");

		List<PatientSearch> queryResult = getPatientSearches(pageable, fromStatement, whereStatement);
		Long queryResultAmount = getPatientSearchesAmount(fromStatement, whereStatement);
        return new PageImpl<>(queryResult, pageable, queryResultAmount);
    }

	private Long getPatientSearchesAmount(String fromStatement, String whereStatement) {
		String selectStatement = "SELECT COUNT(1) ";
		Query query = entityManager.createQuery(selectStatement + fromStatement + whereStatement);
		return (Long) query.getSingleResult();
	}

	private List<PatientSearch> getPatientSearches(Pageable pageable, String fromStatement, String whereStatement) {
		String selectStatement = "SELECT NEW net.pladema.patient.service.domain.PatientSearch(p2, p.id, pt.active, " +
				"pe.nameSelfDetermination) ";
		Query query = entityManager.createQuery(selectStatement + fromStatement + whereStatement)
				.setMaxResults(pageable.getPageSize())
				.setFirstResult(pageable.getPageSize() * pageable.getPageNumber());
		return (List<PatientSearch>) query.getResultList();
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
				.setParameter("emergencyCareStateIds", List.of(EmergencyCareState.EN_ATENCION, EmergencyCareState.EN_ESPERA, EmergencyCareState.CON_ALTA_PACIENTE))
				.setParameter("appointmentStatsIds", List.of(AppointmentState.CANCELLED, AppointmentState.SERVED))
				.setMaxResults(limit)
				.getResultList();
		return result;
	}
}