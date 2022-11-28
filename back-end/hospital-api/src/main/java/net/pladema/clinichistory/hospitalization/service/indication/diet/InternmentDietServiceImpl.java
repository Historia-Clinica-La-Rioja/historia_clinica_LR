package net.pladema.clinichistory.hospitalization.service.indication.diet;

import java.util.Collections;
import java.util.List;

import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.application.createDocument.DocumentFactory;
import ar.lamansys.sgh.shared.infrastructure.input.service.DietDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedIndicationPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.service.indication.diet.domain.InternmentDietBo;

import javax.validation.ConstraintViolationException;

@Service
@Slf4j
@RequiredArgsConstructor
public class InternmentDietServiceImpl implements InternmentDietService {

	private final SharedIndicationPort sharedIndicationPort;

	private final DocumentFactory documentFactory;

	private final InternmentEpisodeService internmentEpisodeService;
	
	private final LocalDateMapper localDateMapper;

	@Override
	public List<DietDto> getInternmentEpisodeDiets(Integer internmentEpisodeId) {
		log.debug("Input parameter -> internmentEpisodeId {}", internmentEpisodeId);
		List<DietDto> result = sharedIndicationPort.getInternmentEpisodeDiets(internmentEpisodeId);
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public DietDto getInternmentEpisodeDiet(Integer dietId) {
		log.debug("Input parameter -> dietId {}", dietId);
		DietDto result = sharedIndicationPort.getInternmentEpisodeDiet(dietId);
		log.debug("Output -> {}", result);
		return result;
	}
	@Override
	public Integer addDiet(InternmentDietBo dietBo) {
		log.debug("Input parameter -> dietBo {}", dietBo);
		assertInternmentEpisodeCanCreateIndication(dietBo.getEncounterId());
		Integer result = sharedIndicationPort.addDiet(mapToDietDto(dietBo));
		dietBo.setId(documentFactory.run(dietBo, false));
		sharedIndicationPort.saveDocument(dietBo.getId(),result);
		log.debug("Output -> {}", result);
		return result;
	}

	private void assertInternmentEpisodeCanCreateIndication(Integer internmentEpisodeId) {
		if(internmentEpisodeService.haveEpicrisis(internmentEpisodeId)) {
			throw new ConstraintViolationException("No se puede crear una indicación debido a que existe una epicrisis", Collections.emptySet());
		}
	}

	private DietDto mapToDietDto(InternmentDietBo dietBo){
		return new DietDto(
				null,
				dietBo.getPatientId(),
				dietBo.getTypeId(),
				dietBo.getStatusId(),
				dietBo.getProfessionalId(),
				null,
				localDateMapper.toDateDto(dietBo.getIndicationDate()),
				localDateMapper.toDateTimeDto(dietBo.getCreatedOn()),
				dietBo.getDescription());
	}
}
