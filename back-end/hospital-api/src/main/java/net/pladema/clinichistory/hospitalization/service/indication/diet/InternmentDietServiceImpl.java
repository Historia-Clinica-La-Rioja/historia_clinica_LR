package net.pladema.clinichistory.hospitalization.service.indication.diet;

import ar.lamansys.sgh.shared.infrastructure.input.service.DietDto;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedIndicationPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class InternmentDietServiceImpl implements InternmentDietService {

	private final SharedIndicationPort sharedIndicationPort;

	@Override
	public List<DietDto> getInternmentEpisodeDiets(Integer internmentEpisodeId) {
		log.debug("Input parameter -> internmentEpisodeId {}",internmentEpisodeId);
		List<DietDto> result = sharedIndicationPort.getInternmentEpisodeDiets(internmentEpisodeId);
		log.debug("Output -> {}", result);
		return result;
	}
}
