package ar.lamansys.refcounterref.infraestructure.output.repository.appointment;

import ar.lamansys.refcounterref.application.port.CounterReferenceAppointmentStorage;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.SharedAppointmentPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CounterReferenceAppointmentStorageImpl implements CounterReferenceAppointmentStorage {

    private final SharedAppointmentPort sharedAppointmentPort;
    private final boolean disableValidation;

    public CounterReferenceAppointmentStorageImpl(@Value("${test.stress.disable.validation:false}") boolean disableValidation,
                                         SharedAppointmentPort sharedAppointmentPort) {
        this.sharedAppointmentPort = sharedAppointmentPort;
        this.disableValidation = disableValidation;
    }

    @Override
    public void run(Integer patientId, Integer doctorId, LocalDate date) {
        if (!disableValidation && sharedAppointmentPort.hasCurrentAppointment(patientId,doctorId,date))
            sharedAppointmentPort.serveAppointment(patientId, doctorId, date);
    }

    @Override
    public Integer getPatientMedicalCoverageId(Integer patientId, Integer doctorId) {
        return sharedAppointmentPort.getMedicalCoverage(patientId, doctorId);
    }

}