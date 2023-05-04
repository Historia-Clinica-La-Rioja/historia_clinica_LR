package net.pladema.clinichistory.indication.service.otherindication;

import java.util.Collections;
import java.util.List;

import javax.validation.ConstraintViolationException;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;

import net.pladema.clinichistory.indication.service.otherindication.domain.GenericOtherIndicationBo;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.application.createDocument.DocumentFactory;
import ar.lamansys.sgh.shared.infrastructure.input.service.NewDosageDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.OtherIndicationDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedIndicationPort;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;

@Service
@Slf4j
@RequiredArgsConstructor
public class OtherIndicationServiceImpl implements OtherIndicationService {

	private final SharedIndicationPort sharedIndicationPort;

	private final DocumentFactory documentFactory;

	private final InternmentEpisodeService internmentEpisodeService;

	private final LocalDateMapper localDateMapper;

	@Override
	public Integer add(GenericOtherIndicationBo otherIndicationBo, Short sourceTypeId) {
		log.debug("Input parameter -> otherIndicationBo {}, sourceTypeId {}", otherIndicationBo, sourceTypeId);
		assertInternmentEpisodeCanCreateIndication(otherIndicationBo.getEncounterId(), sourceTypeId);
		Integer result = sharedIndicationPort.addOtherIndication(toOtherIndicationDto(otherIndicationBo), sourceTypeId);
		otherIndicationBo.setId(documentFactory.run(otherIndicationBo, false));
		sharedIndicationPort.saveDocument(otherIndicationBo.getId(), result);
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public List<OtherIndicationDto> getEpisodeOtherIndications(Integer internmentEpisodeId, Short sourceTypeId){
		log.debug("Input parameter -> internmentEpisodeId {}, sourceTypeId {}", internmentEpisodeId, sourceTypeId);
		List<OtherIndicationDto> result = sharedIndicationPort.getInternmentEpisodeOtherIndications(internmentEpisodeId, sourceTypeId);
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public OtherIndicationDto getOtherIndication(Integer otherIndicationId){
		log.debug("Input parameter -> otherIndicationId {}", otherIndicationId);
		OtherIndicationDto result = sharedIndicationPort.getInternmentEpisodeOtherIndication(otherIndicationId);
		log.debug("Output -> {}", result);
		return result;
	}

	private void assertInternmentEpisodeCanCreateIndication(Integer internmentEpisodeId, Short sourceTypeId) {
		if (internmentEpisodeService.haveEpicrisis(internmentEpisodeId) && sourceTypeId.equals(SourceType.HOSPITALIZATION)) {
			throw new ConstraintViolationException("No se puede crear una indicación debido a que existe una epicrisis", Collections.emptySet());
		}
	}

	private OtherIndicationDto toOtherIndicationDto(GenericOtherIndicationBo bo) {
		NewDosageDto dosageDto = new NewDosageDto();
		dosageDto.setFrequency(bo.getDosageBo().getFrequency());
		dosageDto.setPeriodUnit(bo.getDosageBo().getPeriodUnit());
		dosageDto.setStartDateTime(localDateMapper.toDateTimeDto(bo.getDosageBo().getStartDate()));
		dosageDto.setEvent(bo.getDosageBo().getEvent());
		return new OtherIndicationDto(null, bo.getPatientId(), bo.getTypeId(), bo.getStatusId(), bo.getProfessionalId(), null, localDateMapper.toDateDto(bo.getIndicationDate()), localDateMapper.toDateTimeDto(bo.getCreatedOn()), bo.getOtherIndicationTypeId(), bo.getDescription(), dosageDto, bo.getOtherType());
	}

}
