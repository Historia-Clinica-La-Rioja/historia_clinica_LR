package net.pladema.appointment.repository;

import net.pladema.appointment.repository.entity.AppointmentAssn;
import net.pladema.appointment.repository.entity.AppointmentAssnPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentAssnRepository extends JpaRepository<AppointmentAssn, AppointmentAssnPK> {
}
