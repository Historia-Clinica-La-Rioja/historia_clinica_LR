package net.pladema.internation.service.documents;

import net.pladema.internation.service.internment.summary.domain.PatientDischargeBo;

import java.util.Optional;

public interface PatientDischargeService {

    public Optional<PatientDischargeBo> getPatientDischarge(Integer internmentEpisodeId);
}
