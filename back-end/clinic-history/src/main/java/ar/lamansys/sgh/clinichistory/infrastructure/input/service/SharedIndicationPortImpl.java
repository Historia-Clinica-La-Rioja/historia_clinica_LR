package ar.lamansys.sgh.clinichistory.infrastructure.input.service;

import java.util.List;
import java.util.stream.Collectors;

import ar.lamansys.sgh.clinichistory.application.indication.getinternmentepisodeotherindications.GetInternmentEpisodeOtherIndications;

import ar.lamansys.sgh.shared.infrastructure.input.service.NewDosageDto;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.application.indication.creatediet.CreateDiet;
import ar.lamansys.sgh.clinichistory.application.indication.createotherindication.CreateOtherIndication;
import ar.lamansys.sgh.clinichistory.application.indication.getinternmentepisodediets.GetInternmentEpisodeDiets;
import ar.lamansys.sgh.clinichistory.domain.ips.DietBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DosageBo;
import ar.lamansys.sgh.clinichistory.domain.ips.EUnitsOfTimeBo;
import ar.lamansys.sgh.clinichistory.domain.ips.OtherIndicationBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentIndicationRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentIndication;
import ar.lamansys.sgh.shared.infrastructure.input.service.DietDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.OtherIndicationDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedIndicationPort;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SharedIndicationPortImpl implements SharedIndicationPort {

	private final GetInternmentEpisodeDiets getInternmentEpisodeDiets;

	private final GetInternmentEpisodeOtherIndications getInternmentEpisodeOtherIndications;

	private final CreateDiet createDiet;

	private final CreateOtherIndication createOtherIndication;

	private final DocumentIndicationRepository documentIndicationRepository;

	private final LocalDateMapper localDateMapper;

	@Override
	public List<DietDto> getInternmentEpisodeDiets(Integer internmentEpisodeId) {
		log.debug("Input parameter -> internmentEpisodeId {}", internmentEpisodeId);
		List<DietDto> result = getInternmentEpisodeDiets.run(internmentEpisodeId)
				.stream()
				.map(this::mapToDietDto)
				.collect(Collectors.toList());
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public List<OtherIndicationDto> getInternmentEpisodeOtherIndications(Integer internmentEpisodeId) {
		log.debug("Input parameter -> internmentEpisodeId {}", internmentEpisodeId);
		List<OtherIndicationDto> result = getInternmentEpisodeOtherIndications.run(internmentEpisodeId)
				.stream()
				.map(this::mapToOtherIndicationDto)
				.collect(Collectors.toList());
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public Integer addDiet(DietDto dietDto) {
		log.debug("Input parameter -> dietDto {}", dietDto);
		Integer result = createDiet.run(mapToDietBo(dietDto));
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public Integer addOtherIndication(OtherIndicationDto otherIndicationDto) {
		log.debug("Input parameter -> otherIndicationDto {}", otherIndicationDto);
		Integer result = createOtherIndication.run(mapToOtherIndicationBo(otherIndicationDto));
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public 	void saveDocument(Long id, Integer indicationId){
		log.debug("Input parameter -> id {}, indicationId {}", id, indicationId);
		documentIndicationRepository.save(new DocumentIndication(id,indicationId));
	}

	private OtherIndicationBo mapToOtherIndicationBo(OtherIndicationDto dto) {
		DosageBo dosageBo = new DosageBo();
		dosageBo.setFrequency(dto.getDosage().getFrequency());
		dosageBo.setPeriodUnit(EUnitsOfTimeBo.map(dto.getDosage().getPeriodUnit()));
		dosageBo.setStartDate(localDateMapper.fromDateDto(dto.getIndicationDate()));
		dosageBo.setEndDate(localDateMapper.fromDateDto(dto.getIndicationDate()));
		dosageBo.setEvent(dto.getDosage().getEvent());
		return new OtherIndicationBo(dto.getId(),
				dto.getPatientId(),
				dto.getType().getId(),
				dto.getStatus().getId(),
				null,
				dto.getProfessionalId(),
				localDateMapper.fromDateDto(dto.getIndicationDate()),
				null,
				dto.getOtherIndicationTypeId(),
				dosageBo,
				dto.getDescription(),
				dto.getOtherType());
	}

	private DietBo mapToDietBo(DietDto dto) {
		return new DietBo(dto.getId(),
				dto.getPatientId(),
				dto.getType().getId(),
				dto.getStatus().getId(),
				null,
				dto.getProfessionalId(),
				localDateMapper.fromDateDto(dto.getIndicationDate()),
				localDateMapper.fromDateTimeDto(dto.getCreatedOn()),
				dto.getDescription());
	}

	private DietDto mapToDietDto(DietBo bo){
		return new DietDto(
				bo.getId(),
				bo.getPatientId(),
				bo.getTypeId(),
				bo.getStatusId(),
				bo.getProfessionalId(),
				bo.getCreatedByName(),
				localDateMapper.toDateDto(bo.getIndicationDate()),
				localDateMapper.toDateTimeDto(bo.getCreatedOn()),
				bo.getDescription());
	}

	private OtherIndicationDto mapToOtherIndicationDto(OtherIndicationBo bo){
		NewDosageDto dosageDto = new NewDosageDto();
		DosageBo dosageBo = bo.getDosage();
		dosageDto.setFrequency(dosageBo.getFrequency());
		dosageDto.setPeriodUnit(dosageBo.getPeriodUnit());
		dosageDto.setEvent(dosageBo.getEvent());
		return new OtherIndicationDto(
				bo.getId(),
				bo.getPatientId(),
				bo.getTypeId(),
				bo.getStatusId(),
				bo.getProfessionalId(),
				bo.getCreatedByName(),
				localDateMapper.toDateDto(bo.getIndicationDate()),
				localDateMapper.toDateTimeDto(bo.getCreatedOn()),
				bo.getOtherIndicationTypeId(),
				bo.getDescription(),
				dosageDto,
				bo.getOtherType());
	}
}
