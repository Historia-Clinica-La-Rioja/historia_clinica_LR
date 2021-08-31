package ar.lamansys.immunization.domain.appointment;

import java.time.LocalDate;

public interface ServeAppointmentStorage {

    void run(Integer patientId, Integer id, LocalDate nowDate);
}
