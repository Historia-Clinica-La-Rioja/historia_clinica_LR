package ar.lamansys.sgh.clinichistory.application.fetchHCE;

import ar.lamansys.sgh.clinichistory.domain.hce.HCEAllergyBo;

import java.util.List;

public interface HCEAllergyService {

    List<HCEAllergyBo> getAllergies(Integer patientId);

	List<HCEAllergyBo> getActiveInternmentEpisodeAllergies(Integer institutionId, Integer patientId);
}
