package ar.lamansys.nursing.infrastructure.output.repository;

import ar.lamansys.nursing.application.port.NursingAppointmentStorage;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedAppointmentPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class NursingAppointmentStorageImpl implements NursingAppointmentStorage {

    private final SharedAppointmentPort sharedAppointmentPort;

    private final boolean disableValidation;

    public NursingAppointmentStorageImpl(@Value("${test.stress.disable.validation:false}") boolean disableValidation,
                                         SharedAppointmentPort sharedAppointmentPort) {
        this.sharedAppointmentPort = sharedAppointmentPort;
        this.disableValidation = disableValidation;
    }

    @Override
    public void run(Integer patientId, Integer doctorId, LocalDate date) {
        if (!disableValidation && sharedAppointmentPort.hasConfirmedAppointment(patientId,doctorId,date))
            sharedAppointmentPort.serveAppointment(patientId, doctorId, date);
    }

    @Override
    public Integer getPatientMedicalCoverageId(Integer patientId, Integer doctorId) {
        return sharedAppointmentPort.getMedicalCoverage(patientId, doctorId);
    }
}
