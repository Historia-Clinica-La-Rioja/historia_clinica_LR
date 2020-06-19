package net.pladema.clinichistory.ips.service;

import net.pladema.clinichistory.ips.service.domain.InmunizationBo;

import java.util.List;

public interface InmunizationService {

    List<InmunizationBo> loadInmunization(Integer patientId, Long documentId, List<InmunizationBo> inmunizations);

    List<InmunizationBo> getInmunizationsGeneralState(Integer internmentEpisodeId);
}
