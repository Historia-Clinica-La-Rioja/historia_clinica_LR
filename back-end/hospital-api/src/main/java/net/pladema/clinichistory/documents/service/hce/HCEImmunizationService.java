package net.pladema.clinichistory.documents.service.hce;

import net.pladema.clinichistory.documents.service.hce.domain.HCEImmunizationBo;

import java.util.List;

public interface HCEImmunizationService {

    List<HCEImmunizationBo> getImmunization(Integer patientId);
}
