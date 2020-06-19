package net.pladema.clinichistory.hospitalization.service.patientDischarge;

import net.pladema.clinichistory.hospitalization.service.domain.PatientDischargeBo;

import java.util.Optional;

public interface PatientDischargeService {

    public Optional<PatientDischargeBo> getPatientDischarge(Integer internmentEpisodeId);
}
