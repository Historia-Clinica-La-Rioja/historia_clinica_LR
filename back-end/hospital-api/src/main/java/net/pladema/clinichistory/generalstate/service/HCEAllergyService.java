package net.pladema.clinichistory.generalstate.service;

import net.pladema.clinichistory.generalstate.service.domain.HCEAllergyBo;

import java.util.List;

public interface HCEAllergyService {

    List<HCEAllergyBo> getAllergies(Integer patientId);
}
