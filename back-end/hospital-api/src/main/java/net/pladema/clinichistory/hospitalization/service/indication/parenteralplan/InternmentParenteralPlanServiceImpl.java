package net.pladema.clinichistory.hospitalization.service.indication.parenteralplan;

import ar.lamansys.sgh.clinichistory.application.createDocument.DocumentFactory;
import ar.lamansys.sgh.clinichistory.domain.ips.DosageBo;
import ar.lamansys.sgh.clinichistory.domain.ips.FrequencyBo;
import ar.lamansys.sgh.clinichistory.domain.ips.OtherPharmacoBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.EIndicationStatus;
import ar.lamansys.sgh.shared.infrastructure.input.service.EIndicationType;
import ar.lamansys.sgh.shared.infrastructure.input.service.FrequencyDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.NewDosageDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.OtherPharmacoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.ParenteralPlanDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.QuantityDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedIndicationPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.dates.controller.dto.TimeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.indication.parenteralplan.domain.InternmentParenteralPlanBo;

import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class InternmentParenteralPlanServiceImpl implements InternmentParenteralPlanService {

	private final SharedIndicationPort sharedIndicationPort;

	private final DocumentFactory documentFactory;

	private final LocalDateMapper localDateMapper;

	private final InternmentEpisodeService internmentEpisodeService;

	@Override
	public Integer add(InternmentParenteralPlanBo parenteralPlanBo) {
		log.debug("Input parameter -> parenteralPlanBo {}", parenteralPlanBo);
		assertInternmentEpisodeCanCreateIndication(parenteralPlanBo.getEncounterId());
		Integer result = sharedIndicationPort.addParenteralPlan(toParenteralPlanDto(parenteralPlanBo));
		parenteralPlanBo.setId(documentFactory.run(parenteralPlanBo, false));
		sharedIndicationPort.saveDocument(parenteralPlanBo.getId(), result);
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public List<ParenteralPlanDto> getInternmentEpisodeParenteralPlans(Integer internmentEpisodeId) {
		log.debug("Input parameter -> internmentEpisodeId {}", internmentEpisodeId);
		List<ParenteralPlanDto> result = sharedIndicationPort.getInternmentEpisodeParenteralPlans(internmentEpisodeId);
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public ParenteralPlanDto getInternmentEpisodeParenteralPlan(Integer parenteralPlanId) {
		log.debug("Input parameter -> parenteralPlanId {}", parenteralPlanId);
		ParenteralPlanDto result = sharedIndicationPort.getInternmentEpisodeParenteralPlan(parenteralPlanId);
		log.debug("Output -> {}", result);
		return result;
	}

	private void assertInternmentEpisodeCanCreateIndication(Integer internmentEpisodeId) {
		if (internmentEpisodeService.haveEpicrisis(internmentEpisodeId)) {
			throw new ConstraintViolationException("No se puede crear una indicación debido a que existe una epicrisis", Collections.emptySet());
		}
	}

	private ParenteralPlanDto toParenteralPlanDto (InternmentParenteralPlanBo bo){
		ParenteralPlanDto result = new ParenteralPlanDto();
		result.setSnomed(new SharedSnomedDto(bo.getSnomed().getSctid(), bo.getSnomed().getPt()));
		result.setPharmacos(bo.getPharmacos().stream().map(this::toOtherPharmacoDto).collect(Collectors.toList()));
		result.setDosage(toDosageDto(bo.getDosage()));
		result.setFrequency(toFrequencyDto(bo.getFrequency()));
		result.setVia(bo.getVia());
		result.setPatientId(bo.getPatientId());
		result.setType(EIndicationType.map(bo.getTypeId()));
		result.setStatus(EIndicationStatus.map(bo.getStatusId()));
		result.setProfessionalId(bo.getProfessionalId());
		result.setIndicationDate(localDateMapper.toDateDto(bo.getIndicationDate()));
		result.setCreatedOn(localDateMapper.toDateTimeDto(bo.getCreatedOn()));
		return result;
	}

	private NewDosageDto toDosageDto(DosageBo bo) {
		NewDosageDto result = new NewDosageDto();
		result.setFrequency(bo.getFrequency());
		result.setPeriodUnit(bo.getPeriodUnit());
		result.setStartDateTime(localDateMapper.toDateTimeDto(bo.getStartDate()));
		result.setEvent(bo.getEvent());
		result.setQuantity(new QuantityDto(bo.getQuantity().getValue(), bo.getQuantity().getUnit()));
		return result;
	}

	private OtherPharmacoDto toOtherPharmacoDto(OtherPharmacoBo bo) {
		OtherPharmacoDto result = new OtherPharmacoDto();
		result.setSnomed(new SharedSnomedDto(bo.getSnomed().getSctid(),bo.getSnomed().getPt()));
		result.setDosage(toDosageDto(bo.getDosage()));
		return result;
	}

	private FrequencyDto toFrequencyDto(FrequencyBo bo){
		FrequencyDto result = new FrequencyDto();
		result.setDuration(new TimeDto(bo.getDuration().getHour(), bo.getDuration().getMinute(), bo.getDuration().getSecond()));
		result.setDailyVolume(bo.getDailyVolume());
		result.setFlowDropsHour(bo.getFlowDropsHour());
		result.setFlowMlHour(bo.getFlowMlHour());
		return result;
	}

}
