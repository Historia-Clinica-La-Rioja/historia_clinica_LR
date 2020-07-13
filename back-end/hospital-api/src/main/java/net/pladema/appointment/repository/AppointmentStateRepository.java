package net.pladema.appointment.repository;

import net.pladema.appointment.repository.entity.AppointmentState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentStateRepository extends JpaRepository<AppointmentState, Short> {
}
