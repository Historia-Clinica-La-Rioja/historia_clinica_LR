package net.pladema.clinichistory.requests.medicationrequests.repository;

import ar.lamansys.sgh.shared.infrastructure.input.service.snowstorm.SharedSnowstormSearchDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.snowstorm.SharedSnowstormSearchItemDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.snowstorm.exceptions.SnowstormPortException;
import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.medicationrequests.service.MostFrequentPharmacosStorage;
import net.pladema.clinichistory.requests.medicationrequests.service.domain.SnomedBo;
import net.pladema.snowstorm.controller.service.SnowstormExternalService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class MostFrequentPharmacosStorageImpl implements MostFrequentPharmacosStorage {

	private final MostFrequentPharmacosRepository mostFrequentPharmacosRepository;

	private final SnowstormExternalService snowstormExternalService;

	private static final String OUTPUT = "Output -> {}";


	@Override
	public List<SnomedBo> getMostFrequentPharmacos(Integer institutionId) throws SnowstormPortException {
		log.debug("Input parameters -> institutionId {}", institutionId);
		List<SnomedBo> result = mostFrequentPharmacosRepository.getMostFrequentPharmacos(UserInfo.getCurrentAuditor(), institutionId);
		SharedSnowstormSearchDto sharedSnowstormSearchDto = snowstormExternalService.getConcepts(getSnomedBoIds(result));
		result = toSnomedBo(sharedSnowstormSearchDto.getItems());
		log.debug(OUTPUT, result);
		return result;
	}

	private List<SnomedBo> toSnomedBo(List<SharedSnowstormSearchItemDto> items) {
		return items
				.stream()
				.map(sharedSnowstormSearchItemDto -> new SnomedBo(sharedSnowstormSearchItemDto.getPt(), sharedSnowstormSearchItemDto.getConceptId()))
				.collect(Collectors.toList());
	}

	private List<Long> getSnomedBoIds(List<SnomedBo> result) {
		return result
				.stream()
				.map(snomedBo -> Long.parseLong(snomedBo.getSctid()))
				.collect(Collectors.toList());
	}
}
