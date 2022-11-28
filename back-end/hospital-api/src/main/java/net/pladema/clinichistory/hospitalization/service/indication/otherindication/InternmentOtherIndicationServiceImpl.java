package net.pladema.clinichistory.hospitalization.service.indication.otherindication;

import java.util.Collections;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.application.createDocument.DocumentFactory;
import ar.lamansys.sgh.shared.infrastructure.input.service.NewDosageDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.OtherIndicationDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedIndicationPort;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.indication.otherindication.domain.InternmentOtherIndicationBo;

@Service
@Slf4j
@RequiredArgsConstructor
public class InternmentOtherIndicationServiceImpl implements InternmentOtherIndicationService {

	private final SharedIndicationPort sharedIndicationPort;

	private final DocumentFactory documentFactory;

	private final InternmentEpisodeService internmentEpisodeService;

	private final LocalDateMapper localDateMapper;

	@Override
	public Integer add(InternmentOtherIndicationBo otherIndicationBo) {
		log.debug("Input parameter -> otherIndicationBo {}", otherIndicationBo);
		assertInternmentEpisodeCanCreateIndication(otherIndicationBo.getEncounterId());
		Integer result = sharedIndicationPort.addOtherIndication(toOtherIndicationDto(otherIndicationBo));
		otherIndicationBo.setId(documentFactory.run(otherIndicationBo, false));
		sharedIndicationPort.saveDocument(otherIndicationBo.getId(), result);
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public List<OtherIndicationDto> getInternmentEpisodeOtherIndications(Integer internmentEpisodeId){
		log.debug("Input parameter -> internmentEpisodeId {}", internmentEpisodeId);
		List<OtherIndicationDto> result = sharedIndicationPort.getInternmentEpisodeOtherIndications(internmentEpisodeId);
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public OtherIndicationDto getInternmentEpisodeOtherIndication(Integer otherIndicationId){
		log.debug("Input parameter -> otherIndicationId {}", otherIndicationId);
		OtherIndicationDto result = sharedIndicationPort.getInternmentEpisodeOtherIndication(otherIndicationId);
		log.debug("Output -> {}", result);
		return result;
	}

	private void assertInternmentEpisodeCanCreateIndication(Integer internmentEpisodeId) {
		if (internmentEpisodeService.haveEpicrisis(internmentEpisodeId)) {
			throw new ConstraintViolationException("No se puede crear una indicación debido a que existe una epicrisis", Collections.emptySet());
		}
	}

	private OtherIndicationDto toOtherIndicationDto(InternmentOtherIndicationBo bo) {
		NewDosageDto dosageDto = new NewDosageDto();
		dosageDto.setFrequency(bo.getDosageBo().getFrequency());
		dosageDto.setPeriodUnit(bo.getDosageBo().getPeriodUnit());
		dosageDto.setStartDateTime(localDateMapper.toDateTimeDto(bo.getDosageBo().getStartDate()));
		dosageDto.setEvent(bo.getDosageBo().getEvent());
		return new OtherIndicationDto(null, bo.getPatientId(), bo.getTypeId(), bo.getStatusId(), bo.getProfessionalId(), null, localDateMapper.toDateDto(bo.getIndicationDate()), localDateMapper.toDateTimeDto(bo.getCreatedOn()), bo.getOtherIndicationTypeId(), bo.getDescription(), dosageDto, bo.getOtherType());
	}

}
