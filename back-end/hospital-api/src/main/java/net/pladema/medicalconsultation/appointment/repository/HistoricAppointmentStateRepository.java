package net.pladema.medicalconsultation.appointment.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.pladema.medicalconsultation.appointment.repository.entity.HistoricAppointmentState;
import net.pladema.medicalconsultation.appointment.repository.entity.HistoricAppointmentStatePK;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface HistoricAppointmentStateRepository extends SGXAuditableEntityJPARepository<HistoricAppointmentState, HistoricAppointmentStatePK> {

    @Transactional(readOnly = true)
    @Query("SELECT CASE WHEN count(*) > 1 THEN TRUE ELSE FALSE END " +
            "FROM HistoricAppointmentState has " +
            "WHERE has.pk.appointmentId = :appointmentId " +
            "AND has.appointmentStateId = " + AppointmentState.CONFIRMED)
    boolean hasHistoricallyConfirmedAtLeastOnes(@Param("appointmentId") Integer appointmentId);

}
