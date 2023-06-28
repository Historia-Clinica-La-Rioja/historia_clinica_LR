package net.pladema.patient.repository;

import ar.lamansys.sgh.shared.infrastructure.input.service.patient.enums.EPatientType;
import net.pladema.clinichistory.hospitalization.repository.domain.InternmentEpisodeStatus;
import net.pladema.emergencycare.repository.entity.EmergencyCareState;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.patient.controller.dto.PatientSearchFilter;
import net.pladema.patient.repository.entity.Patient;
import net.pladema.patient.repository.entity.PatientType;
import net.pladema.patient.service.domain.PatientSearch;
import ar.lamansys.sgx.shared.repositories.QueryPart;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

import static net.pladema.patient.repository.PatientSearchQuery.AND_JOINING_OPERATOR;
import static net.pladema.patient.repository.PatientSearchQuery.LIKE_COMPARATOR;

@Repository
public class PatientRepositoryImpl implements PatientRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    public PatientRepositoryImpl(EntityManager entityManager){
        super();
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<PatientSearch> getAllByOptionalFilter(PatientSearchFilter searchFilter, Integer resultSize) {
        PatientSearchQuery patientSearchQuery = new PatientSearchQuery(searchFilter);
        QueryPart queryPart = new QueryPart(
                "SELECT ")
                .concatPart(patientSearchQuery.select())
                .concat(" FROM ")
                .concatPart(patientSearchQuery.from())
                .concat("WHERE ")
                .concatPart(patientSearchQuery.whereWithAllAttributes(AND_JOINING_OPERATOR, LIKE_COMPARATOR))
				.concat(" AND patient.deleted = false AND patient.type_id != " + EPatientType.REJECTED.getId() + " ");

        if (searchFilter.getFilterByNameSelfDetermination()) {
			queryPart.concat("UNION ");
			queryPart.concatPart(patientSearchQuery.addUnion());
		}

		Query query = entityManager.createNativeQuery(queryPart.toString());
		query.setMaxResults(resultSize);
		queryPart.configParams(query);

        return patientSearchQuery.construct(query.getResultList());
    }

    public Integer getCountByOptionalFilter(PatientSearchFilter searchFilter) {
        PatientSearchQuery patientSearchQuery = new PatientSearchQuery(searchFilter);

        QueryPart queryPart = new QueryPart(
                "SELECT COUNT(DISTINCT result.id) \n")
                .concat("FROM ( \n")
				.concat("	SELECT patient.id as id \n")
				.concat("	FROM \n")
                .concatPart(patientSearchQuery.from())
                .concat("	WHERE \n")
                .concatPart(patientSearchQuery.whereWithAllAttributes(AND_JOINING_OPERATOR, LIKE_COMPARATOR))
				.concat(" AND patient.deleted = false ");

        if (searchFilter.getFilterByNameSelfDetermination()) {
			queryPart.concat("UNION \n" +
					"SELECT patient.id as id \n");
			queryPart.concat("FROM \n")
					.concatPart(patientSearchQuery.from())
					.concat("WHERE \n")
					.concatPart(patientSearchQuery.whereWithAllAttributesAndNameSelfDetermination(AND_JOINING_OPERATOR, LIKE_COMPARATOR))
					.concat(" AND patient.deleted = false ");
		}

        queryPart.concat(") as result");

        Query query = entityManager.createNativeQuery(queryPart.toString());
		queryPart.configParams(query);

		BigInteger result = (BigInteger) query.getSingleResult();
		return result.intValue();
    }

	@Override
	public List<Patient> getLongTermTemporaryPatientIds(LocalDateTime maxDate, Short limit) {
		String sqlQuery = "SELECT p " +
				"FROM Patient p " +
				"WHERE p.typeId = :typeId AND p.id IN (" +
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
				"WHERE a.dateTypeId >= CURRENT_DATE " +
				"AND a.appointmentStateId NOT IN (:appointmentStatsIds) " +
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