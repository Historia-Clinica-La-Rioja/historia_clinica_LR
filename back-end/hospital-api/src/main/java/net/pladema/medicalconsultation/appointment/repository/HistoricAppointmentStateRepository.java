package net.pladema.medicalconsultation.appointment.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import org.springframework.stereotype.Repository;

import net.pladema.medicalconsultation.appointment.repository.entity.HistoricAppointmentState;
import net.pladema.medicalconsultation.appointment.repository.entity.HistoricAppointmentStatePK;

@Repository
public interface HistoricAppointmentStateRepository extends SGXAuditableEntityJPARepository<HistoricAppointmentState, HistoricAppointmentStatePK> {
	
}
