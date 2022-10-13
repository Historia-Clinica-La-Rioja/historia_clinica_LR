package ar.lamansys.refcounterref.infraestructure.output.repository.referenceappointment;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReferenceAppointmentRepository extends SGXAuditableEntityJPARepository<ReferenceAppointment, ReferenceAppointmentPk> {

	@Query(value = "SELECT COUNT(1) " +
			"FROM ReferenceAppointment ra " +
			"LEFT JOIN AppointmentAssn aa on ra.pk.appointmentId = aa.pk.appointmentId " +
			"LEFT JOIN Appointment a on aa.pk.appointmentId  = a.id " +
			"WHERE aa.pk.diaryId = :diaryId " +
			"AND a.dateTypeId = :day " +
			"AND a.appointmentStateId <> :appointmentStateId " +
			"AND ra.deleteable.deleted = false")
	Integer getAssignedProtectedAppointmentsQuantity(@Param("diaryId") Integer diaryId,
													 @Param("day") LocalDate day,
													 @Param("appointmentStateId") Short appointmentStateId);

	@Transactional(readOnly = true)
	@Query(value = "SELECT ra.pk.appointmentId " +
			"FROM ReferenceAppointment ra " +
			"JOIN AppointmentAssn asn ON ra.pk.appointmentId = asn.pk.appointmentId	" +
			"WHERE asn.pk.diaryId IN (:diaryIds) " +
			"AND ra.deleteable.deleted = false")
	List<Integer> findAppointmentIdsByDiaryIds(@Param("diaryIds") List<Integer> diaryIds);

}
