package net.pladema.internation.service.internment;

import net.pladema.internation.service.internment.domain.BasicListedPatientBo;
import net.pladema.internation.service.internment.domain.InternmentEpisodeBo;
import net.pladema.internation.service.internment.domain.InternmentEpisodeProcessBo;

import java.util.List;

public interface InternmentPatientService {

    List<BasicListedPatientBo> getInternmentPatients(Integer institutionId);

    List<InternmentEpisodeBo> getAllInternmentPatient(Integer institutionId);

    InternmentEpisodeProcessBo internmentEpisodeInProcess(Integer institutionId, Integer patientId);
}
