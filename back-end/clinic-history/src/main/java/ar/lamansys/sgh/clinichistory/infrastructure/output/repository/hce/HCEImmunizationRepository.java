package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity.HCEImmunizationVo;

import java.util.List;

public interface HCEImmunizationRepository {

    List<HCEImmunizationVo> getImmunization(Integer patientId);
}
