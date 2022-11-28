package ar.lamansys.immunization.infrastructure.output.repository.appointments;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ar.lamansys.immunization.domain.appointment.AppointmentStorage;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.SharedAppointmentPort;

@Service
public class ServeAppointmentStorageImpl implements AppointmentStorage {

    private final SharedAppointmentPort sharedAppointmentPort;

    private final boolean disableValidation;

    public ServeAppointmentStorageImpl(@Value("${test.stress.disable.validation:false}") boolean disableValidation,
                                       SharedAppointmentPort sharedAppointmentPort) {
        this.sharedAppointmentPort = sharedAppointmentPort;
        this.disableValidation = disableValidation;
    }

    @Override
    public Integer run(Integer patientId, Integer doctorId, LocalDate date) {
        if (!disableValidation && sharedAppointmentPort.hasCurrentAppointment(patientId,doctorId,date))
            return sharedAppointmentPort.serveAppointment(patientId, doctorId, date);
		return null;
    }

    @Override
    public Integer getPatientMedicalCoverageId(Integer patientId, Integer doctorId) {
        return sharedAppointmentPort.getMedicalCoverage(patientId, doctorId);
    }
}
