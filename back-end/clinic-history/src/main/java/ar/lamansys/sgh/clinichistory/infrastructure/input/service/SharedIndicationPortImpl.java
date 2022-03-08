package ar.lamansys.sgh.clinichistory.infrastructure.input.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.application.indication.getinternmentepisodediets.GetInternmentEpisodeDiets;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.mapper.HCEGeneralStateMapper;
import ar.lamansys.sgh.shared.infrastructure.input.service.DietDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedIndicationPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SharedIndicationPortImpl implements SharedIndicationPort {

	private final GetInternmentEpisodeDiets getInternmentEpisodeDiets;

	private final HCEGeneralStateMapper hceGeneralStateMapper;

	@Override
	public List<DietDto> getInternmentEpisodeDiets(Integer internmentEpisodeId) {
		log.debug("Input parameter -> internmentEpisodeId {}", internmentEpisodeId);
		List<DietDto> result = hceGeneralStateMapper.toListDietDto(getInternmentEpisodeDiets.run(internmentEpisodeId));
		log.debug("Output -> {}", result);
		return result;
	}
}
