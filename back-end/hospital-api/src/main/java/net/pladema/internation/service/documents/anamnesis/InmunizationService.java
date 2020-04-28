package net.pladema.internation.service.documents.anamnesis;

import net.pladema.internation.service.domain.ips.InmunizationBo;

import java.util.List;

public interface InmunizationService {

    List<InmunizationBo> loadInmunization(Integer patientId, Long documentId, List<InmunizationBo> inmunizations);

    List<InmunizationBo> getInmunizationsGeneralState(Integer internmentEpisodeId);
}
