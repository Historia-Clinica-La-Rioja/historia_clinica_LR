package ar.lamansys.sgh.clinichistory.infrastructure.input.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import ar.lamansys.sgh.clinichistory.application.indication.createpharmaco.CreatePharmaco;
import ar.lamansys.sgh.clinichistory.application.indication.getInternmentEpisodeOtherIndication.GetInternmentEpisodeOtherIndication;
import ar.lamansys.sgh.clinichistory.application.indication.getInternmentEpisodeParenteralPlan.GetInternmentEpisodeParenteralPlan;
import ar.lamansys.sgh.clinichistory.application.indication.getInternmentEpisodePharmaco.GetInternmentEpisodePharmaco;
import ar.lamansys.sgh.clinichistory.application.indication.getinternmentepisodediet.GetInternmentEpisodeDiet;
import ar.lamansys.sgh.clinichistory.application.indication.getinternmentepisodenursingrecords.GetInternmentEpisodeNursingRecords;
import ar.lamansys.sgh.clinichistory.application.indication.getinternmentepisodeotherindications.GetInternmentEpisodeOtherIndications;

import ar.lamansys.sgh.clinichistory.application.indication.getinternmentepisodeparenteralplans.GetInternmentEpisodeParenteralPlans;
import ar.lamansys.sgh.clinichistory.application.indication.getinternmentepisodepharamacos.GetInternmentEpisodePharmacos;
import ar.lamansys.sgh.clinichistory.application.indication.updatenursingrecordstatus.UpdateNursingRecordStatus;
import ar.lamansys.sgh.clinichistory.domain.ips.FrequencyBo;
import ar.lamansys.sgh.clinichistory.domain.ips.IndicationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.NursingRecordBo;
import ar.lamansys.sgh.clinichistory.domain.ips.PharmacoSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.ips.QuantityBo;
import ar.lamansys.sgh.clinichistory.domain.ips.OtherPharmacoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.PharmacoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.EIndicationType;
import ar.lamansys.sgh.shared.infrastructure.input.service.ENursingRecordStatus;
import ar.lamansys.sgh.shared.infrastructure.input.service.FrequencyDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.IndicationDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.NewDosageDto;

import ar.lamansys.sgh.shared.infrastructure.input.service.NursingRecordDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.OtherPharmacoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.PharmacoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.PharmacoSummaryDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.QuantityDto;
import ar.lamansys.sgh.clinichistory.application.indication.createparenteralplan.CreateParenteralPlan;

import ar.lamansys.sgh.clinichistory.domain.ips.ParenteralPlanBo;

import ar.lamansys.sgh.shared.infrastructure.input.service.ParenteralPlanDto;
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
	private final GetInternmentEpisodeDiet getInternmentEpisodeDiet;

	private final GetInternmentEpisodeOtherIndications getInternmentEpisodeOtherIndications;
	private final GetInternmentEpisodeOtherIndication getInternmentEpisodeOtherIndication;

	private final GetInternmentEpisodePharmacos getInternmentEpisodePharmacos;
	private final GetInternmentEpisodePharmaco getInternmentEpisodePharmaco;

	private final CreateDiet createDiet;

	private final CreateOtherIndication createOtherIndication;

	private final CreatePharmaco createPharmaco;

	private final DocumentIndicationRepository documentIndicationRepository;

	private final LocalDateMapper localDateMapper;

	private final CreateParenteralPlan createParenteralPlan;

	private final GetInternmentEpisodeParenteralPlans getInternmentEpisodeParenteralPlans;
	private final GetInternmentEpisodeParenteralPlan getInternmentEpisodeParenteralPlan;

	private final GetInternmentEpisodeNursingRecords getInternmentEpisodeNursingRecords;

	private final UpdateNursingRecordStatus updateNursingRecordStatus;

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
	public DietDto getInternmentEpisodeDiet(Integer dietId) {
		log.debug("Input parameter -> dietId {}", dietId);
		DietDto result = mapToDietDto(getInternmentEpisodeDiet.run(dietId));
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
	public OtherIndicationDto getInternmentEpisodeOtherIndication(Integer otherIndicationId) {
		log.debug("Input parameter -> otherIndicationId {}", otherIndicationId);
		OtherIndicationDto result = mapToOtherIndicationDto(getInternmentEpisodeOtherIndication.run(otherIndicationId));
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public List<PharmacoSummaryDto> getInternmentEpisodePharmacos(Integer internmentEpisodeId) {
		log.debug("Input parameter -> internmentEpisodeId {}", internmentEpisodeId);
		List<PharmacoSummaryDto> result = getInternmentEpisodePharmacos.run(internmentEpisodeId).stream().map(this::mapToPharmacoSummaryDto).collect(Collectors.toList());
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public PharmacoDto getInternmentEpisodePharmaco(Integer pharmacoId) {
		log.debug("Input parameter -> pharmacoId {}", pharmacoId);
		PharmacoDto result = mapToPharmacoDto(getInternmentEpisodePharmaco.run(pharmacoId));
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
	public Integer addParenteralPlan(ParenteralPlanDto dto) {
		log.debug("Input parameter -> parenteralPlanDto {}", dto);
		Integer result = createParenteralPlan.run(mapToParenteralPlanBo(dto));
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public List<ParenteralPlanDto> getInternmentEpisodeParenteralPlans(Integer internmentEpisodeId) {
		log.debug("Input parameter -> internmentEpisodeId {}", internmentEpisodeId);
		List<ParenteralPlanDto> result = getInternmentEpisodeParenteralPlans.run(internmentEpisodeId).stream().map(this::mapToParenteralPlanDto).collect(Collectors.toList());
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public ParenteralPlanDto getInternmentEpisodeParenteralPlan(Integer parenteralPlanId) {
		log.debug("Input parameter -> parenteralPlanId {}", parenteralPlanId);
		ParenteralPlanDto result = mapToParenteralPlanDto(getInternmentEpisodeParenteralPlan.run(parenteralPlanId));
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public List<NursingRecordDto> getInternmentEpisodeNursingRecords(Integer internmentEpisodeId) {
		log.debug("Input parameter -> internmentEpisodeId {}", internmentEpisodeId);
		List<NursingRecordDto> result = getInternmentEpisodeNursingRecords.run(internmentEpisodeId).stream().map(this::mapToNursingRecordDto).collect(Collectors.toList());
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public boolean updateNursingRecordStatus(Integer nursingRecordId, String status, LocalDateTime administrationTime, Integer userId, String reason) {
		log.debug("Input parameter -> nursingRecordId {}, statusId {}, administrationTime {}, userId {}, reason {}", nursingRecordId, status, administrationTime, userId, reason);
		boolean result = updateNursingRecordStatus.run(nursingRecordId, status, administrationTime, userId, reason);
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
				dto.getSolvent() != null ? toOtherPharmacoBo(dto.getSolvent(), dto.getIndicationDate()) : null,
				dto.getHealthConditionId(),
				dto.getFoodRelationId(),
				dto.getPatientProvided(),
				dto.getViaId(),
				dto.getNote());
	}

	private OtherIndicationBo mapToOtherIndicationBo(OtherIndicationDto dto) {
		DosageBo dosageBo = new DosageBo();
		dosageBo.setFrequency(dto.getDosage().getFrequency());
		if (dto.getDosage().getPeriodUnit() != null)
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
		if(dto.getPeriodUnit()!=null)
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

	private OtherPharmacoDto toOtherPharmacoDto(OtherPharmacoBo bo){
		OtherPharmacoDto result = new OtherPharmacoDto();
		result.setSnomed(new SharedSnomedDto(bo.getSnomed().getSctid(), bo.getSnomed().getPt()));
		result.setDosage(toDosageDto(bo.getDosage()));
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
		result.setStartDateTime(localDateMapper.toDateTimeDto(bo.getStartDate()));
		if(bo.getQuantity() != null)
			result.setQuantity(new QuantityDto(bo.getQuantity().getValue(), bo.getQuantity().getUnit()));
		return result;
	}

	private ParenteralPlanBo mapToParenteralPlanBo(ParenteralPlanDto dto) {
		DosageBo dosage = toDosageBo(dto.getDosage(), dto.getIndicationDate());
		FrequencyBo frequency = toFrequencyBo(dto.getFrequency());
		SnomedBo snomed = new SnomedBo(dto.getSnomed().getSctid(), dto.getSnomed().getPt());
		List<OtherPharmacoBo> pharmacos = dto.getPharmacos().stream().map(p-> toOtherPharmacoBo(p, dto.getIndicationDate())).collect(Collectors.toList());

		return new ParenteralPlanBo(dto.getId(),
				dto.getPatientId(),
				dto.getType().getId(),
				dto.getStatus().getId(),
				null,
				dto.getProfessionalId(),
				localDateMapper.fromDateDto(dto.getIndicationDate()),
				null,
				snomed,
				dosage,
				frequency,
				dto.getVia(),
				pharmacos);
	}

	private FrequencyBo toFrequencyBo(FrequencyDto dto){
		FrequencyBo result = new FrequencyBo();
		result.setFlowMlHour(dto.getFlowMlHour());
		result.setFlowDropsHour(dto.getFlowDropsHour());
		result.setDailyVolume(dto.getDailyVolume());
		if (dto.getDuration()!=null)
			result.setDuration(LocalTime.of(
					dto.getDuration().getHours()==null ? 0 : dto.getDuration().getHours(),
					dto.getDuration().getMinutes()==null ? 0 : dto.getDuration().getMinutes(),
					dto.getDuration().getSeconds()==null ? 0 : dto.getDuration().getSeconds()));
		return result;
	}

	private FrequencyDto toFrequencyDto(FrequencyBo bo){
		FrequencyDto result = new FrequencyDto();
		result.setFlowMlHour(bo.getFlowMlHour());
		result.setFlowDropsHour(bo.getFlowDropsHour());
		result.setDailyVolume(bo.getDailyVolume());
		if (bo.getDuration()!=null)
			result.setDuration(new TimeDto(bo.getDuration().getHour(), bo.getDuration().getMinute(), bo.getDuration().getSecond()));
		return result;
	}

	private ParenteralPlanDto mapToParenteralPlanDto(ParenteralPlanBo bo){
		NewDosageDto dosageDto = new NewDosageDto();
		DosageBo dosageBo = bo.getDosage();
		dosageDto.setFrequency(dosageBo.getFrequency());
		dosageDto.setPeriodUnit(dosageBo.getPeriodUnit());
		dosageDto.setEvent(dosageBo.getEvent());
		dosageDto.setStartDateTime(localDateMapper.toDateTimeDto(dosageBo.getStartDate()));
		dosageDto.setQuantity(new QuantityDto(dosageBo.getQuantity().getValue(), dosageBo.getQuantity().getUnit()));
		SharedSnomedDto snomedDto = new SharedSnomedDto(bo.getSnomed().getSctid(), bo.getSnomed().getPt());
		FrequencyDto frequencyDto = toFrequencyDto(bo.getFrequency());
		List<OtherPharmacoDto> otherPharmacos = bo.getPharmacos().stream().map(p-> toOtherPharmacoDto(p)).collect(Collectors.toList());

		return new ParenteralPlanDto(
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
				frequencyDto,
				bo.getVia(),
				otherPharmacos);
	}

	private PharmacoDto mapToPharmacoDto(PharmacoBo bo){
		NewDosageDto dosageDto = toDosageDto(bo.getDosage());

		return new PharmacoDto(bo.getId(),
				bo.getPatientId(),
				bo.getTypeId(),
				bo.getStatusId(),
				bo.getProfessionalId(),
				bo.getCreatedByName(),
				localDateMapper.toDateDto(bo.getIndicationDate()),
				localDateMapper.toDateTimeDto(bo.getCreatedOn()),
				bo.getSnomed() != null ? new SharedSnomedDto(bo.getSnomed().getSctid(), bo.getSnomed().getPt()) : null,
				dosageDto,
				bo.getSolvent() != null ? toOtherPharmacoDto(bo.getSolvent()) : null,
				bo.getHealthConditionId(),
				bo.getFoodRelationId(),
				bo.getPatientProvided(),
				bo.getViaId(),
				bo.getNote());
	}

	private NursingRecordDto mapToNursingRecordDto(NursingRecordBo bo){
		NursingRecordDto result = new NursingRecordDto();
		IndicationDto indicationDto = mapToIndicationDto(bo.getIndication());
		result.setIndication(indicationDto);
		result.setEvent(bo.getEvent());
		result.setId(bo.getId());
		result.setStatus(ENursingRecordStatus.map(bo.getStatusId()));
		result.setObservation(bo.getObservation());
		result.setScheduledAdministrationTime(localDateMapper.toDateTimeDto(bo.getScheduledAdministrationTime()));
		result.setAdministrationTime(localDateMapper.toDateTimeDto(bo.getAdministrationTime()));
		result.setUpdatedBy(bo.getUpdatedByName());
		result.setUpdateReason(bo.getUpdateReason());
		return result;
	}

	private IndicationDto mapToIndicationDto(IndicationBo indication){
		switch (EIndicationType.map(indication.getTypeId())){
			case DIET:{
				DietBo dietBo = (DietBo) indication;
				return mapToDietDto(dietBo);
			}
			case OTHER_INDICATION:{
				OtherIndicationBo oiBo = (OtherIndicationBo) indication;
				return mapToOtherIndicationDto(oiBo);
			}
			case PARENTERAL_PLAN:{
				ParenteralPlanBo ppBo = (ParenteralPlanBo) indication;
				return mapToParenteralPlanDto(ppBo);
			}
			default:{
				PharmacoBo pharmacoBo = (PharmacoBo) indication;
				return mapToPharmacoDto(pharmacoBo);
				}
		}
	}

}
