package net.pladema.clinichistory.ips.service;

import net.pladema.clinichistory.ips.service.domain.InmunizationBo;

import java.util.List;

public interface ImmunizationService {

    List<InmunizationBo> loadInmunization(Integer patientId, Long documentId, List<InmunizationBo> inmunizations);
}
