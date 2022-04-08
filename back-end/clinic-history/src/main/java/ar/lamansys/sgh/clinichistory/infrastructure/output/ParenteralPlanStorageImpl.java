package ar.lamansys.sgh.clinichistory.infrastructure.output;

import ar.lamansys.sgh.clinichistory.application.ports.ParenteralPlanStorage;
import ar.lamansys.sgh.clinichistory.domain.ips.DosageBo;
import ar.lamansys.sgh.clinichistory.domain.ips.EUnitsOfTimeBo;
import ar.lamansys.sgh.clinichistory.domain.ips.FrequencyBo;
import ar.lamansys.sgh.clinichistory.domain.ips.OtherPharmacoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ParenteralPlanBo;
import ar.lamansys.sgh.clinichistory.domain.ips.QuantityBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.domain.ips.services.SnomedService;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.DosageRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.FrequencyRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.OtherPharmacoRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.ParenteralPlanRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.QuantityRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.Dosage;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.Quantity;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.indication.Frequency;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.indication.OtherPharmaco;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.indication.ParenteralPlan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ParenteralPlanStorageImpl implements ParenteralPlanStorage {

	private final ParenteralPlanRepository parenteralPlanRepository;
	private final DosageRepository dosageRepository;
	private final QuantityRepository quantityRepository;
	private final SnomedService snomedService;
	private final FrequencyRepository frequencyRepository;
	private final OtherPharmacoRepository otherPharmacoRepository;

	@Override
	public Integer createParenteralPlan(ParenteralPlanBo bo) {
		Long quantity = quantityRepository.save(mapToQuantity(bo.getDosage().getQuantity())).getId();
		Integer dosageId = dosageRepository.save(mapToDosage(bo.getDosage(), quantity)).getId();
		Integer frequencyId = frequencyRepository.save(mapToFrequency(bo.getFrequency())).getId();
		bo.getDosage().setId(dosageId);
		bo.getFrequency().setId(frequencyId);
		Integer parenteralPlanId = parenteralPlanRepository.save(mapToEntity(bo)).getId();
		bo.getPharmacos().forEach(p -> {
			p.setIndicationId(parenteralPlanId);
			saveOtherPharmaco(p);
		});
		log.debug("Output -> {}", parenteralPlanId);
		return parenteralPlanId;
	}

	private void saveOtherPharmaco (OtherPharmacoBo bo){
		OtherPharmaco otherPharmaco = new OtherPharmaco();
		Long quantity = quantityRepository.save(mapToQuantity(bo.getDosage().getQuantity())).getId();
		Integer dosageId = dosageRepository.save(mapToDosage(bo.getDosage(), quantity)).getId();
		Integer snomedId = snomedService.getSnomedId(bo.getSnomed())
				.orElseGet(() -> snomedService.createSnomedTerm(bo.getSnomed()));
		otherPharmaco.setDosageId(dosageId);
		otherPharmaco.setSnomedId(snomedId);
		otherPharmaco.setIndicationId(bo.getIndicationId());
		otherPharmacoRepository.save(otherPharmaco);
	}

	private Frequency mapToFrequency (FrequencyBo bo){
		Frequency result = new Frequency();
		result.setDailyVolume(bo.getDailyVolume());
		result.setDuration(bo.getDuration());
		result.setFlowDropsHour(bo.getFlowDropsHour());
		result.setFlowMlHour(bo.getFlowMlHour());
		return result;
	}

	private Dosage mapToDosage(DosageBo bo, Long quantity){
		Dosage result = new Dosage();
		result.setFrequency(bo.getFrequency());
		result.setPeriodUnit(bo.getPeriodUnit());
		result.setStartDate(bo.getStartDate());
		result.setEndDate(bo.getEndDate());
		result.setEvent(bo.getEvent());
		result.setDoseQuantityId(quantity);
		return result;
	}

	private DosageBo mapToDosageBo(Dosage entity) {
		DosageBo result = new DosageBo();
		result.setId(entity.getId());
		result.setFrequency(entity.getFrequency());
		if(entity.getPeriodUnit()!= null)
			result.setPeriodUnit(EUnitsOfTimeBo.map(entity.getPeriodUnit()));
		result.setStartDate(entity.getStartDate());
		result.setEndDate(entity.getEndDate());
		result.setEvent(entity.getEvent());
		quantityRepository.findById(entity.getDoseQuantityId()).ifPresent( quantity ->
				result.setQuantity(new QuantityBo(quantity.getValue().intValue(), quantity.getUnit())));
		return result;
	}

	private Quantity mapToQuantity(QuantityBo bo){
		Quantity result = new Quantity();
		result.setValue(bo.getValue().doubleValue());
		result.setUnit(bo.getUnit());
		return result;
	}

	private ParenteralPlan mapToEntity (ParenteralPlanBo bo){
		ParenteralPlan result = new ParenteralPlan();
		Integer snomedId = snomedService.getSnomedId(bo.getSnomed())
				.orElseGet(() -> snomedService.createSnomedTerm(bo.getSnomed()));
		result.setSnomedId(snomedId);
		result.setDosageId(bo.getDosage().getId());
		result.setViaId(bo.getVia());
		result.setId(bo.getId());
		result.setFrequencyId(bo.getFrequency().getId());
		result.setPatientId(bo.getPatientId());
		result.setTypeId(bo.getTypeId());
		result.setStatusId(bo.getStatusId());
		result.setIndicationDate(bo.getIndicationDate());
		result.setProfessionalId(bo.getProfessionalId());
		return result;
	}

	private ParenteralPlanBo mapToBo(ParenteralPlan entity){
		SnomedBo snomedBo = snomedService.getSnomed(entity.getSnomedId());
		Optional<Dosage> dosage = dosageRepository.findById(entity.getDosageId());
		FrequencyBo frequencyBo = frequencyRepository.findById(entity.getFrequencyId()).map(this::mapToFrequencyBo).get();
		return new ParenteralPlanBo(entity.getId(),
				entity.getPatientId(),
				entity.getTypeId(),
				entity.getStatusId(),
				entity.getCreatedBy(),
				entity.getProfessionalId(),
				entity.getIndicationDate(),
				entity.getCreatedOn(),
				snomedBo,
				dosage.isPresent() ? mapToDosageBo(dosage.get()) : null,
				frequencyBo,
				entity.getViaId(), new ArrayList<>());
	}

	private FrequencyBo mapToFrequencyBo (Frequency entity){
		FrequencyBo result = new FrequencyBo();
		result.setId(entity.getId());
		result.setDuration(entity.getDuration());
		result.setDailyVolume(entity.getDailyVolume());
		result.setFlowDropsHour(entity.getFlowDropsHour());
		result.setFlowMlHour(entity.getFlowMlHour());
		return result;
	}


}
