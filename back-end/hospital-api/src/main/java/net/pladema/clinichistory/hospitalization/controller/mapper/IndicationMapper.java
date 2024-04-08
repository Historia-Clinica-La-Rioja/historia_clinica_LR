package net.pladema.clinichistory.hospitalization.controller.mapper;

import ar.lamansys.sgh.clinichistory.domain.ips.DocumentObservationsBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DosageBo;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EUnitsOfTimeBo;
import ar.lamansys.sgh.clinichistory.domain.ips.FrequencyBo;
import ar.lamansys.sgh.clinichistory.domain.ips.OtherPharmacoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.QuantityBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.DietDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.FrequencyDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.IndicationDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.NewDosageDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.OtherIndicationDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.OtherPharmacoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.ParenteralPlanDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.PharmacoDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import ar.lamansys.sgx.shared.dates.controller.dto.TimeDto;
import lombok.AllArgsConstructor;
import net.pladema.clinichistory.indication.service.diet.domain.GenericDietBo;
import net.pladema.clinichistory.indication.service.diet.domain.GenericIndicationBo;
import net.pladema.clinichistory.indication.service.otherindication.domain.GenericOtherIndicationBo;
import net.pladema.clinichistory.indication.service.parenteralplan.domain.GenericParenteralPlanBo;
import net.pladema.clinichistory.indication.service.pharmaco.domain.GenericPharmacoBo;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class IndicationMapper {

	private final LocalDateMapper localDateMapper;

	public GenericDietBo mapToDietBo(DietDto dto, Integer institutionId, Integer internmentEpisodeId, Short sourceTypeId) {
		GenericDietBo result = new GenericDietBo();
		result.setDescription(dto.getDescription());
		return (GenericDietBo) setIndicationInfoBo(dto, result, institutionId, internmentEpisodeId, sourceTypeId);
	}

	public GenericOtherIndicationBo mapToOtherIndicationBo(OtherIndicationDto dto, Integer institutionId, Integer internmentEpisodeId, Short sourceTypeId) {
		GenericOtherIndicationBo result = new GenericOtherIndicationBo();
		result.setDescription(dto.getDescription());
		result.setOtherIndicationTypeId(dto.getOtherIndicationTypeId());
		result.setOtherType(dto.getOtherType());
		result.setDosageBo(toDosageBo(dto.getDosage(),dto.getIndicationDate()));
		return (GenericOtherIndicationBo) setIndicationInfoBo(dto, result, institutionId, internmentEpisodeId, sourceTypeId);
	}

	public GenericPharmacoBo mapToPharmacoBo(PharmacoDto dto, Integer institutionId, Integer internmentEpisodeId, Short sourceTypeId) {
		GenericPharmacoBo result = new GenericPharmacoBo();
		result.setSnomed(dto.getSnomed() != null? new SnomedBo(dto.getSnomed().getSctid(), dto.getSnomed().getPt()) : null);
		result.setDosage(toDosageBo(dto.getDosage(),dto.getIndicationDate()));
		if (dto.getSolvent() != null)
			result.setSolvent(toOtherPharmacoBo(dto.getSolvent(), dto.getIndicationDate()));
		result.setHealthConditionId(dto.getHealthConditionId());
		result.setFoodRelationId(dto.getFoodRelationId());
		result.setPatientProvided(dto.getPatientProvided());
		result.setViaId(dto.getViaId());
		DocumentObservationsBo documentObservationsBo = new DocumentObservationsBo();
		documentObservationsBo.setOtherNote(dto.getNote());
		result.setNotes(documentObservationsBo);
		return (GenericPharmacoBo) setIndicationInfoBo(dto, result, institutionId, internmentEpisodeId, sourceTypeId);
	}

	public GenericParenteralPlanBo mapToInternmentParenteralPlanBo (ParenteralPlanDto dto, Integer institutionId, Integer internmentEpisodeId, Short sourceTypeId){
		GenericParenteralPlanBo result = new GenericParenteralPlanBo();
		result.setDosage(toDosageBo(dto.getDosage(), dto.getIndicationDate()));
		result.setFrequency(mapToFrequencyBo(dto.getFrequency()));
		result.setSnomed(new SnomedBo(dto.getSnomed().getSctid(), dto.getSnomed().getPt()));
		result.setVia(dto.getVia());
		result.setPharmacos(dto.getPharmacos().stream().
				map(p -> toOtherPharmacoBo(p, dto.getIndicationDate())).collect(Collectors.toList()));
		return (GenericParenteralPlanBo) setIndicationInfoBo(dto, result, institutionId, internmentEpisodeId, sourceTypeId);
	}

	private GenericIndicationBo setIndicationInfoBo (IndicationDto dto, GenericIndicationBo bo, Integer institutionId, Integer internmentEpisodeId, Short sourceTypeId) {
		bo.setPatientId(dto.getPatientId());
		bo.setTypeId(dto.getType().getId());
		bo.setProfessionalId(dto.getProfessionalId());
		bo.setStatusId(dto.getStatus().getId());
		bo.setIndicationDate(localDateMapper.fromDateDto(dto.getIndicationDate()));
		bo.setInstitutionId(institutionId);
		bo.setEncounterId(internmentEpisodeId);
		bo.setSourceTypeId(sourceTypeId);
		return bo;
	}

	private DosageBo toDosageBo(NewDosageDto dto, DateDto indicationDate) {
		DosageBo result = new DosageBo();
		result.setFrequency(dto.getFrequency());
		if(dto.getPeriodUnit() != null)
			result.setPeriodUnit(EUnitsOfTimeBo.map(dto.getPeriodUnit()));
		LocalDateTime startDate = (dto.getStartDateTime()!=null)
				? localDateMapper.fromDateTimeDto(dto.getStartDateTime())
				: localDateMapper.fromDateTimeDto(new DateTimeDto(indicationDate, new TimeDto(0,0,0)));
		result.setStartDate(startDate);
		result.setEndDate(startDate.plusDays(1).toLocalDate().atStartOfDay());
		result.setEvent(dto.getEvent());
		if(dto.getQuantity() != null)
			result.setQuantity(new QuantityBo(dto.getQuantity().getValue(), dto.getQuantity().getUnit()));
		return result;
	}

	private OtherPharmacoBo toOtherPharmacoBo(OtherPharmacoDto dto, DateDto indicationDate) {
		OtherPharmacoBo result = new OtherPharmacoBo();
		result.setSnomed(new SnomedBo(dto.getSnomed().getSctid(), dto.getSnomed().getPt()));
		result.setDosage(toDosageBo(dto.getDosage(), indicationDate));
		return result;
	}

	private FrequencyBo mapToFrequencyBo(FrequencyDto frequency){
		FrequencyBo result = new FrequencyBo();
		result.setFlowMlHour(frequency.getFlowMlHour());
		result.setFlowDropsHour(frequency.getFlowDropsHour());
		result.setDailyVolume(frequency.getDailyVolume());
		if (frequency.getDuration()!=null)
			result.setDuration(LocalTime.of(
					frequency.getDuration().getHours()==null ? 0 : frequency.getDuration().getHours(),
					frequency.getDuration().getMinutes()==null ? 0 : frequency.getDuration().getMinutes(),
					frequency.getDuration().getSeconds()==null ? 0 : frequency.getDuration().getSeconds()));
		return result;
	}

}
