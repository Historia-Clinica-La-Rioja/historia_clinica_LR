package net.pladema.clinichistory.hospitalization.service;

import net.pladema.clinichistory.hospitalization.service.domain.BasicListedPatientBo;
import net.pladema.clinichistory.hospitalization.service.domain.InternmentEpisodeBo;
import net.pladema.clinichistory.hospitalization.service.domain.InternmentEpisodeProcessBo;

import java.util.List;
import java.util.Optional;

public interface InternmentPatientService {

    List<BasicListedPatientBo> getInternmentPatients(Integer institutionId);

    List<InternmentEpisodeBo> getAllInternmentPatient(Integer institutionId);

    InternmentEpisodeProcessBo internmentEpisodeInProcess(Integer institutionId, Integer patientId);

	Optional<Integer> getInternmentEpisodeIdInProcess(Integer institutionId, Integer patientId);
}
