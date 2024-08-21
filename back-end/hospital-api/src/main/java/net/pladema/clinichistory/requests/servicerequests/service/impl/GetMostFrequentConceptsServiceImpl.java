package net.pladema.clinichistory.requests.servicerequests.service.impl;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.service.GetMostFrequentConceptsService;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetMostFrequentConceptsServiceImpl implements GetMostFrequentConceptsService {

	@Override
	public List<SharedSnomedDto> getMostFrequentStudies(Integer institutionId){
		log.debug("Input parameter -> institutionId {}", institutionId);

		List<SharedSnomedDto> result = new ArrayList<>();

		log.debug("Output -> {}", result);
		return result;
	}
}
