package net.pladema.clinichistory.documents.repository.hce;

import net.pladema.clinichistory.documents.repository.hce.domain.HCEAllergyVo;

import java.util.List;

public interface HCEAllergyIntoleranceRepository {

    List<HCEAllergyVo> findAllergies(Integer patientId);
}
