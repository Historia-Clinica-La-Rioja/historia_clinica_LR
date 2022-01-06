package net.pladema.snvs.application.ports.patient;

import net.pladema.snvs.domain.patient.PatientDataBo;

import java.util.Optional;

public interface PatientStorage {
    Optional<PatientDataBo> getPatientInfo(Integer patientId);
}
