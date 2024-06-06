package ar.lamansys.refcounterref.infraestructure.output.repository.referenceappointment;

import ar.lamansys.refcounterref.domain.referenceappointment.ReferenceAppointmentBo;
import ar.lamansys.refcounterref.domain.referenceappointment.ReferenceAppointmentSummaryBo;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReferenceAppointmentRepository extends SGXAuditableEntityJPARepository<ReferenceAppointment, ReferenceAppointmentPk> {

	@Transactional(readOnly = true)
	@Query(value = "SELECT ra.pk.appointmentId " +
			"FROM ReferenceAppointment ra " +
			"JOIN AppointmentAssn asn ON ra.pk.appointmentId = asn.pk.appointmentId	" +
			"WHERE asn.pk.diaryId IN (:diaryIds) " +
			"AND ra.deleteable.deleted = false " +
			"AND ra.protectedAppointment IS TRUE ")
	List<Integer> findAppointmentIdsByDiaryIds(@Param("diaryIds") List<Integer> diaryIds);

	@Transactional(readOnly = true)
	@Query(value = "SELECT (CASE WHEN COUNT(ra.pk.appointmentId) > 0 THEN TRUE ELSE FALSE END) " +
			"FROM ReferenceAppointment ra " +
			"WHERE ra.deleteable.deleted = false " +
			"AND ra.pk.appointmentId = :appointmentId " +
			"AND ra.protectedAppointment IS TRUE ")
	boolean isProtectedAppointment(@Param("appointmentId") Integer appointmentId);

	@Transactional
	@Modifying
	@Query(value = "UPDATE ReferenceAppointment ra "
			+ "SET ra.deleteable.deleted = true "
			+ ", ra.deleteable.deletedOn = CURRENT_TIMESTAMP "
			+ ", ra.deleteable.deletedBy = ?#{ principal.userId } "
			+ "WHERE ra.pk.appointmentId = :appointmentId" )
	void deleteByAppointmentId(@Param("appointmentId") Integer appointmentId);

	@Transactional(readOnly = true)
	@Query(value = "SELECT (CASE WHEN COUNT(ra.pk.appointmentId) > 0 THEN TRUE ELSE FALSE END) " +
			"FROM ReferenceAppointment ra " +
			"JOIN AppointmentAssn asn ON (ra.pk.appointmentId = asn.pk.appointmentId) " +
			"JOIN Appointment a ON (asn.pk.appointmentId = a.id) " +
			"WHERE asn.pk.openingHoursId = :openingHourId " +
			"AND a.appointmentStateId IN (:appointmentStates) " +
			"AND ra.deleteable.deleted = false " +
			"AND ra.protectedAppointment IS TRUE")
	boolean existsInOpeningHour(@Param("openingHourId") Integer openingHourId,
								@Param("appointmentStates") List<Short> appointmentStates);

	@Transactional(readOnly = true)
	@Query(value = "SELECT new ar.lamansys.refcounterref.domain.referenceappointment.ReferenceAppointmentBo(ra.pk.referenceId, ra.pk.appointmentId) " +
			"FROM ReferenceAppointment ra " +
			"JOIN Appointment a ON (ra.pk.appointmentId = a.id) " +
			"WHERE ra.pk.referenceId IN (:referenceIds) " +
			"AND a.appointmentStateId <> :appointmentStateId " +
			"AND ra.deleteable.deleted = false")
	List<ReferenceAppointmentBo> getAppointmentIdsByReferenceIds(@Param("referenceIds") List<Integer> referenceIds,
																 @Param("appointmentStateId") Short appointmentStateId);

	@Transactional(readOnly = true)
	@Query(value = "SELECT new ar.lamansys.refcounterref.domain.referenceappointment.ReferenceAppointmentBo(ra.pk.referenceId, ra.pk.appointmentId) " +
			"FROM ReferenceAppointment ra " +
			"JOIN Appointment a ON (ra.pk.appointmentId = a.id) " +
			"WHERE ra.pk.referenceId IN (:referenceIds) " +
			"AND ra.deleteable.deleted = false")
	List<ReferenceAppointmentBo> getAppointmentIdsByReferenceIds(@Param("referenceIds") List<Integer> referenceIds);
	
	@Transactional(readOnly = true)
	@Query(value = "SELECT new ar.lamansys.refcounterref.domain.referenceappointment.ReferenceAppointmentSummaryBo(ra.pk.referenceId, a.appointmentStateId, cr.closureTypeId) " +
			"FROM ReferenceAppointment ra " +
			"JOIN Appointment a ON (a.id = ra.pk.appointmentId) " +
			"JOIN Reference r ON (ra.pk.referenceId = r.id) " +
			"LEFT JOIN CounterReference cr ON (r.id = cr.referenceId) " +
			"WHERE ra.deleteable.deleted = false " +
			"AND ra.pk.appointmentId = :appointmentId ")
	Optional<ReferenceAppointmentSummaryBo> getReferenceByAppointmentId(@Param("appointmentId")Integer appointmentId);

}
