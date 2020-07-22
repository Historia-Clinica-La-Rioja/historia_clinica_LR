package net.pladema.clinichistory.ips.service;

import net.pladema.clinichistory.ips.service.domain.ImmunizationBo;

import java.util.List;

public interface ImmunizationService {

    List<ImmunizationBo> loadImmunization(Integer patientId, Long documentId, List<ImmunizationBo> inmunizations);
}
