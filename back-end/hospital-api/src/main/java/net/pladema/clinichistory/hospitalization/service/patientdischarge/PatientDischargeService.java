package net.pladema.clinichistory.hospitalization.service.patientdischarge;

import net.pladema.clinichistory.hospitalization.service.domain.PatientDischargeBo;

import java.util.Optional;

public interface PatientDischargeService {

    Optional<PatientDischargeBo> getPatientDischarge(Integer internmentEpisodeId);

    boolean existsById(Integer internmentEpisodeId);
}
