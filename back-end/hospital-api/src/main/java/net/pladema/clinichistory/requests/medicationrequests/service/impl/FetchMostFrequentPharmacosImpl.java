package net.pladema.clinichistory.requests.medicationrequests.service.impl;

import ar.lamansys.sgh.shared.infrastructure.input.service.snowstorm.exceptions.SnowstormPortException;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.medicationrequests.service.FetchMostFrequentPharmacos;

import net.pladema.clinichistory.requests.medicationrequests.service.MostFrequentPharmacosStorage;

import net.pladema.clinichistory.requests.medicationrequests.service.domain.SnomedBo;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class FetchMostFrequentPharmacosImpl implements FetchMostFrequentPharmacos {

	private final MostFrequentPharmacosStorage mostFrequentPharmacosStorage;

	private static final String OUTPUT = "Output -> {}";

	public FetchMostFrequentPharmacosImpl(MostFrequentPharmacosStorage mostFrequentPharmacosStorage) {
		this.mostFrequentPharmacosStorage = mostFrequentPharmacosStorage;
	}

	@Override
	public List<SnomedBo> run(Integer institutionId) throws SnowstormPortException {
		log.debug("Input parameters -> institutionId {}", institutionId);
		List<SnomedBo> result = mostFrequentPharmacosStorage.getMostFrequentPharmacos(institutionId);
		log.debug(OUTPUT, result);
		return result;
	}
}
