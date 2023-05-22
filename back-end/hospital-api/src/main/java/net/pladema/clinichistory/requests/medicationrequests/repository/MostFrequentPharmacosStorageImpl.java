package net.pladema.clinichistory.requests.medicationrequests.repository;

import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.medicationrequests.service.MostFrequentPharmacosStorage;

import net.pladema.clinichistory.requests.medicationrequests.service.domain.SnomedBo;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MostFrequentPharmacosStorageImpl implements MostFrequentPharmacosStorage {

	private final MostFrequentPharmacosRepository mostFrequentPharmacosRepository;

	private static final String OUTPUT = "Output -> {}";

	public MostFrequentPharmacosStorageImpl(MostFrequentPharmacosRepository mostFrequentPharmacosRepository) {
		this.mostFrequentPharmacosRepository = mostFrequentPharmacosRepository;
	}

	@Override
	public List<SnomedBo> getMostFrequentPharmacos(Integer institutionId) {
		log.debug("Input parameters -> institutionId {}", institutionId);
		List<SnomedBo> result = mostFrequentPharmacosRepository.getMostFrequentPharmacos(UserInfo.getCurrentAuditor(), institutionId);
		log.debug(OUTPUT, result);
		return result;
	}
}
