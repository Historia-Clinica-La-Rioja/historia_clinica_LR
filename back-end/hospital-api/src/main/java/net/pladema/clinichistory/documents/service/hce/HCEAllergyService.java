package net.pladema.clinichistory.documents.service.hce;

import ar.lamansys.sgh.clinichistory.domain.hce.HCEAllergyBo;

import java.util.List;

public interface HCEAllergyService {

    List<HCEAllergyBo> getAllergies(Integer patientId);
}
