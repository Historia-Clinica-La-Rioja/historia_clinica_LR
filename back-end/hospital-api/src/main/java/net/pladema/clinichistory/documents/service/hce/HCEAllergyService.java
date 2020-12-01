package net.pladema.clinichistory.documents.service.hce;

import net.pladema.clinichistory.documents.service.hce.domain.HCEAllergyBo;

import java.util.List;

public interface HCEAllergyService {

    List<HCEAllergyBo> getAllergies(Integer patientId);
}
