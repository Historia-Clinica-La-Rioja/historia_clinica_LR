package net.pladema.clinichistory.documents.service.ips;

import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AllergyConditionBo;

import java.util.List;

public interface AllergyService {

    List<AllergyConditionBo> loadAllergies(PatientInfoBo patientInfo, Long id, List<AllergyConditionBo> allergy);
}
