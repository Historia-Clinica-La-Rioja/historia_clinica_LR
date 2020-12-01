package net.pladema.clinichistory.documents.service.ips;

import net.pladema.clinichistory.documents.service.ips.domain.ImmunizationBo;

import java.util.List;

public interface ImmunizationService {

    List<ImmunizationBo> loadImmunization(Integer patientId, Long documentId, List<ImmunizationBo> immunizations);
}
