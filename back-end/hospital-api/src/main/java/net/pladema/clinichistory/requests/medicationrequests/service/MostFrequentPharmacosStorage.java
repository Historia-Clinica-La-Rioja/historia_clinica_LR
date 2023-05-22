package net.pladema.clinichistory.requests.medicationrequests.service;

import net.pladema.clinichistory.requests.medicationrequests.service.domain.SnomedBo;

import java.util.List;

public interface MostFrequentPharmacosStorage {

	List<SnomedBo> getMostFrequentPharmacos(Integer institutionId);
}
