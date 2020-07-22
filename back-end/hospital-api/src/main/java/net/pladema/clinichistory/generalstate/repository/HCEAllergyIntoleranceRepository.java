package net.pladema.clinichistory.generalstate.repository;

import net.pladema.clinichistory.generalstate.repository.domain.HCEAllergyVo;

import java.util.List;

public interface HCEAllergyIntoleranceRepository {

    List<HCEAllergyVo> findAllergies(Integer patientId);
}
