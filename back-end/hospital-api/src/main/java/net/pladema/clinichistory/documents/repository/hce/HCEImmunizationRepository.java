package net.pladema.clinichistory.documents.repository.hce;

import net.pladema.clinichistory.documents.repository.hce.domain.HCEImmunizationVo;

import java.util.List;

public interface HCEImmunizationRepository {

    List<HCEImmunizationVo> getImmunization(Integer patientId);
}
