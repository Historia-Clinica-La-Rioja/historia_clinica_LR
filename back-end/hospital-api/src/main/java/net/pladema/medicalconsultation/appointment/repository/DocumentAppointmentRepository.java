package net.pladema.medicalconsultation.appointment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.pladema.medicalconsultation.appointment.repository.entity.DocumentAppointment;
import net.pladema.medicalconsultation.appointment.repository.entity.DocumentAppointmentPK;
import net.pladema.medicalconsultation.appointment.service.domain.DocumentAppointmentBo;

@Repository
public interface DocumentAppointmentRepository extends JpaRepository<DocumentAppointment, DocumentAppointmentPK> {

	@Transactional(readOnly = true)
	@Query( "SELECT NEW net.pladema.medicalconsultation.appointment.service.domain.DocumentAppointmentBo(" +
			"da.pk.documentId, da.pk.appointmentId) " +
			"FROM DocumentAppointment da " +
			"WHERE da.pk.appointmentId = :appointmentId "
	)
	Optional<DocumentAppointmentBo> getDocumentAppointmentByAppointmentId(@Param("appointmentId") Integer appointmentId);

	@Transactional(readOnly = true)
	@Query( "SELECT NEW net.pladema.medicalconsultation.appointment.service.domain.DocumentAppointmentBo(" +
			"da.pk.documentId, da.pk.appointmentId) " +
			"FROM DocumentAppointment da " +
			"WHERE da.pk.documentId = :documentId "
	)
	Optional<DocumentAppointmentBo> getDocumentAppointmentByDocumentId(@Param("documentId")	Long documentId);
}
