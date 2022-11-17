package ar.lamansys.odontology.infrastructure.repository.consultation;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ar.lamansys.odontology.domain.consultation.OdontologyAppointmentStorage;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.SharedAppointmentPort;

@Service
public class OdontologyOdontologyAppointmentStorageImpl implements OdontologyAppointmentStorage {

    private static final Logger LOG = LoggerFactory.getLogger(OdontologyOdontologyAppointmentStorageImpl.class);

    private final SharedAppointmentPort sharedAppointmentPort;

    private final boolean disableValidation;

    public OdontologyOdontologyAppointmentStorageImpl(@Value("${test.stress.disable.validation:false}") boolean disableValidation,
													  SharedAppointmentPort sharedAppointmentPort) {
        this.sharedAppointmentPort = sharedAppointmentPort;
        this.disableValidation = disableValidation;
    }

    @Override
    public Integer getPatientMedicalCoverageId(Integer patientId, Integer doctorId) {
        LOG.debug("Input parameters -> patientId {}, doctorId {}", patientId, doctorId);
        Integer result = sharedAppointmentPort.getMedicalCoverage(patientId, doctorId);
        LOG.debug("Output -> {}", result);
        return result;
    }

    @Override
    public Integer serveAppointment(Integer patientId, Integer doctorId, LocalDate date) {
        LOG.debug("Input parameters -> patientId {}, doctorId {}, date {}", patientId, doctorId, date);
        if (!disableValidation && sharedAppointmentPort.hasCurrentAppointment(patientId,doctorId,date))
            return sharedAppointmentPort.serveAppointment(patientId, doctorId, date);
		return null;
    }
}
