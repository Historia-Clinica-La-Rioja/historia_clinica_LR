package net.pladema.clinichistory.documents.service.ips;

import net.pladema.clinichistory.documents.service.ips.domain.AllergyConditionBo;

import java.util.List;

public interface AllergyService {

    List<AllergyConditionBo> loadAllergies(Integer patientId, Long id, List<AllergyConditionBo> allergy);
}
