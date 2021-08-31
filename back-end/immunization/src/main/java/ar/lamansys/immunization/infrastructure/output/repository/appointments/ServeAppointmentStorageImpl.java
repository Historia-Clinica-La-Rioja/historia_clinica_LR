package ar.lamansys.immunization.infrastructure.output.repository.appointments;

import ar.lamansys.immunization.domain.appointment.ServeAppointmentStorage;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedAppointmentPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ServeAppointmentStorageImpl implements ServeAppointmentStorage {

    private final SharedAppointmentPort sharedAppointmentPort;

    private final boolean disableValidation;

    public ServeAppointmentStorageImpl(@Value("${test.stress.disable.validation:false}") boolean disableValidation,
                                       SharedAppointmentPort sharedAppointmentPort) {
        this.sharedAppointmentPort = sharedAppointmentPort;
        this.disableValidation = disableValidation;
    }

    @Override
    public void run(Integer patientId, Integer doctorId, LocalDate date) {
        if (!disableValidation && sharedAppointmentPort.hasConfirmedAppointment(patientId,doctorId,date))
            sharedAppointmentPort.serveAppointment(patientId, doctorId, date);
    }
}
