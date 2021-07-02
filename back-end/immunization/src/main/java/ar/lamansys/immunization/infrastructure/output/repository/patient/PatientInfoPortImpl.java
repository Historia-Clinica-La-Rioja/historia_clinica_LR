package ar.lamansys.immunization.infrastructure.output.repository.patient;

import ar.lamansys.immunization.domain.patient.PatientInfoBo;
import ar.lamansys.immunization.domain.patient.PatientInfoPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPatientPort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientInfoPortImpl implements PatientInfoPort {

    private final SharedPatientPort sharedPatientPort;

    public PatientInfoPortImpl(SharedPatientPort sharedPatientPort) {
        this.sharedPatientPort = sharedPatientPort;
    }

    @Override
    public Optional<PatientInfoBo> getPatientInfo(Integer patientId) {
        return Optional.ofNullable(sharedPatientPort.getBasicDataFromPatient(patientId))
                .map(p -> new PatientInfoBo(p.getId(), p.getPerson().getBirthDate()));
    }
}
