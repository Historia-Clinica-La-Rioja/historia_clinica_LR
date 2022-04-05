package ar.lamansys.sgh.clinichistory.infrastructure.input.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import ar.lamansys.sgh.clinichistory.application.indication.createpharmaco.CreatePharmaco;
import ar.lamansys.sgh.clinichistory.application.indication.getinternmentepisodeotherindications.GetInternmentEpisodeOtherIndications;

import ar.lamansys.sgh.clinichistory.application.indication.getinternmentepisodepharamacos.GetInternmentEpisodePharmacos;
import ar.lamansys.sgh.clinichistory.domain.ips.PharmacoSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.ips.QuantityBo;
import ar.lamansys.sgh.clinichistory.domain.ips.OtherPharmacoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.PharmacoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.EIndicationType;
import ar.lamansys.sgh.shared.infrastructure.input.service.NewDosageDto;

import ar.lamansys.sgh.shared.infrastructure.input.service.OtherPharmacoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.PharmacoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.PharmacoSummaryDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.QuantityDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;

import ar.lamansys.sgx.shared.dates.controller.dto.TimeDto;

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

	private final GetInternmentEpisodePharmacos getInternmentEpisodePharmacos;

	private final CreateDiet createDiet;

	private final CreateOtherIndication createOtherIndication;

	private final CreatePharmaco createPharmaco;

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
	public List<PharmacoSummaryDto> getInternmentEpisodePharmacos(Integer internmentEpisodeId) {
		log.debug("Input parameter -> internmentEpisodeId {}", internmentEpisodeId);
		List<PharmacoSummaryDto> result = getInternmentEpisodePharmacos.run(internmentEpisodeId)
				.stream()
				.map(this::mapToPharmacoSummaryDto)
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
	public Integer addPharmaco(PharmacoDto pharmacoDto) {
		log.debug("Input parameter -> pharmacoDto {}", pharmacoDto);
		Integer result = createPharmaco.run(mapToPharmacoBo(pharmacoDto));
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public 	void saveDocument(Long id, Integer indicationId){
		log.debug("Input parameter -> id {}, indicationId {}", id, indicationId);
		documentIndicationRepository.save(new DocumentIndication(id,indicationId));
	}

	private PharmacoBo mapToPharmacoBo(PharmacoDto dto) {
		DosageBo dosageBo = toDosageBo(dto.getDosage(), dto.getIndicationDate());
		OtherPharmacoBo solventBo = toOtherPharmacoBo(dto.getSolvent(), dto.getIndicationDate());

		return new PharmacoBo(dto.getId(),
				dto.getPatientId(),
				dto.getType().getId(),
				dto.getStatus().getId(),
				null,
				dto.getProfessionalId(),
				localDateMapper.fromDateDto(dto.getIndicationDate()),
				null,
				dto.getSnomed() != null ? new SnomedBo(dto.getSnomed().getSctid(), dto.getSnomed().getPt()) : null,
				dosageBo,
				solventBo,
				dto.getHealthConditionId(),
				dto.getFoodRelationId(),
				dto.getPatientProvided(),
				dto.getViaId(),
				dto.getNote());
	}

	private OtherIndicationBo mapToOtherIndicationBo(OtherIndicationDto dto) {
		DosageBo dosageBo = new DosageBo();
		dosageBo.setFrequency(dto.getDosage().getFrequency());
		dosageBo.setPeriodUnit(EUnitsOfTimeBo.map(dto.getDosage().getPeriodUnit()));
		LocalDateTime startDate = (dto.getDosage().getStartDateTime()!=null)
				? localDateMapper.fromDateTimeDto(dto.getDosage().getStartDateTime())
				: localDateMapper.fromDateTimeDto(new DateTimeDto(dto.getIndicationDate(), new TimeDto(0,0,0)));
		dosageBo.setStartDate(startDate);
		dosageBo.setEndDate(startDate.plusDays(1).toLocalDate().atStartOfDay());
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
		dosageDto.setStartDateTime(localDateMapper.toDateTimeDto(dosageBo.getStartDate()));
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

	private DosageBo toDosageBo(NewDosageDto dto, DateDto indicationDate) {
		DosageBo dosageBo = new DosageBo();
		dosageBo.setFrequency(dto.getFrequency());
		dosageBo.setPeriodUnit(EUnitsOfTimeBo.map(dto.getPeriodUnit()));
		LocalDateTime startDate = (dto.getStartDateTime()!=null)
				? localDateMapper.fromDateTimeDto(dto.getStartDateTime())
				: localDateMapper.fromDateTimeDto(new DateTimeDto(indicationDate, new TimeDto(0,0,0)));
		dosageBo.setStartDate(startDate);
		dosageBo.setEndDate(startDate.plusDays(1).toLocalDate().atStartOfDay());
		dosageBo.setEvent(dto.getEvent());
		dosageBo.setQuantity(new QuantityBo(dto.getQuantity().getValue(), dto.getQuantity().getUnit()));
		return dosageBo;
	}

	private OtherPharmacoBo toOtherPharmacoBo(OtherPharmacoDto dto, DateDto indicationDate) {
		OtherPharmacoBo result = new OtherPharmacoBo();
		result.setSnomed(new SnomedBo(dto.getSnomed().getSctid(), dto.getSnomed().getPt()));
		result.setDosage(toDosageBo(dto.getDosage(), indicationDate));
		return result;
	}

	private PharmacoSummaryDto mapToPharmacoSummaryDto(PharmacoSummaryBo bo) {
		NewDosageDto dosageDto = toDosageDto(bo.getDosage());
		SharedSnomedDto snomedDto = new SharedSnomedDto(bo.getSnomed().getSctid(), bo.getSnomed().getPt());

		return new PharmacoSummaryDto(
				bo.getId(),
				bo.getPatientId(),
				bo.getTypeId(),
				bo.getStatusId(),
				bo.getProfessionalId(),
				bo.getCreatedByName(),
				localDateMapper.toDateDto(bo.getIndicationDate()),
				localDateMapper.toDateTimeDto(bo.getCreatedOn()),
				snomedDto,
				dosageDto,
				bo.getVia());
	}

	private NewDosageDto toDosageDto(DosageBo bo) {
		NewDosageDto result = new NewDosageDto();
		result.setFrequency(bo.getFrequency());
		result.setPeriodUnit(bo.getPeriodUnit());
		result.setEvent(bo.getEvent());
		if(bo.getQuantity() != null)
			result.setQuantity(new QuantityDto(bo.getQuantity().getValue(), bo.getQuantity().getUnit()));
		return result;
	}
}
