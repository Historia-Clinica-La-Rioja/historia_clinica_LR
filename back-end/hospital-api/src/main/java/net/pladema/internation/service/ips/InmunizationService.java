package net.pladema.internation.service.ips;

import net.pladema.internation.service.ips.domain.InmunizationBo;

import java.util.List;

public interface InmunizationService {

    List<InmunizationBo> loadInmunization(Integer patientId, Long documentId, List<InmunizationBo> inmunizations);

    List<InmunizationBo> getInmunizationsGeneralState(Integer internmentEpisodeId);
}
