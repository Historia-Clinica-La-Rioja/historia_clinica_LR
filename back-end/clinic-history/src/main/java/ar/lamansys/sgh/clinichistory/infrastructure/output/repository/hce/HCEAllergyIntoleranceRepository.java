package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity.HCEAllergyVo;

import java.util.List;

public interface HCEAllergyIntoleranceRepository {

    List<HCEAllergyVo> findAllergies(Integer patientId);

	HCEAllergyVo findHCEAllergy(Integer allergyId);
}
