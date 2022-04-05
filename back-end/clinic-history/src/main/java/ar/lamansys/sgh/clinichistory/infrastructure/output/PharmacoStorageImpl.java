package ar.lamansys.sgh.clinichistory.infrastructure.output;

import ar.lamansys.sgh.clinichistory.application.ports.PharmacoStorage;
import ar.lamansys.sgh.clinichistory.domain.ips.QuantityBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DosageBo;
import ar.lamansys.sgh.clinichistory.domain.ips.OtherPharmacoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.PharmacoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.domain.ips.services.SnomedService;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.DosageRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.OtherPharmacoRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.PharmacoRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.QuantityRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.Dosage;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.Quantity;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.indication.OtherPharmaco;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.indication.Pharmaco;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PharmacoStorageImpl implements PharmacoStorage {

	private final QuantityRepository quantityRepository;
	private final DosageRepository dosageRepository;
	private final PharmacoRepository pharmacoRepository;
	private final OtherPharmacoRepository otherPharmacoRepository;

	private final SnomedService snomedService;

	@Override
	public Integer createPharmaco(PharmacoBo pharmacoBo) {
		log.debug("Input parameter -> pharmacoBo {}", pharmacoBo);
		Long quantity = quantityRepository.save(mapToQuantity(pharmacoBo.getDosage().getQuantity())).getId();
		Integer dosageId = dosageRepository.save(mapToDosage(pharmacoBo.getDosage(), quantity)).getId();
		pharmacoBo.getDosage().setId(dosageId);
		Integer pharmacoId = pharmacoRepository.save(mapToEntity(pharmacoBo)).getId();
		Integer solventId = saveSolvent(pharmacoBo.getSolvent(), pharmacoId);
		log.debug("pharmacoId {}, solventId {} -> ", pharmacoId, solventId);
		log.debug("Output -> {}", pharmacoId);
		return pharmacoId;
	}

	private Integer saveSolvent(OtherPharmacoBo bo, Integer indicationId) {
		Long quantityId = quantityRepository.save(mapToQuantity(bo.getDosage().getQuantity())).getId();
		Integer dosageId = dosageRepository.save(mapToDosage(bo.getDosage(), quantityId)).getId();
		Integer result = otherPharmacoRepository.save(mapToOtherPharmaco(indicationId, bo.getSnomed(), dosageId)).getId();
		return result;
	}

	private Quantity mapToQuantity(QuantityBo bo){
		Quantity result = new Quantity();
		result.setValue(bo.getValue().doubleValue());
		result.setUnit(bo.getUnit());
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

	private Pharmaco mapToEntity(PharmacoBo bo) {
		Integer snomedId = snomedService.getSnomedId(bo.getSnomed())
				.orElseGet(() -> snomedService.createSnomedTerm(bo.getSnomed()));

		Pharmaco result = new Pharmaco();
		result.setId(bo.getId());
		result.setPatientId(bo.getPatientId());
		result.setTypeId(bo.getTypeId());
		result.setStatusId(bo.getStatusId());
		result.setIndicationDate(bo.getIndicationDate());
		result.setProfessionalId(bo.getProfessionalId());
		result.setSnomedId(snomedId);
		result.setDosageId(bo.getDosage().getId());
		result.setHealthConditionId(bo.getHealthConditionId());
		result.setFoodRelationId(bo.getFoodRelationId().shortValue());
		result.setPatientProvided(bo.getPatientProvided());
		result.setViaId(bo.getViaId().shortValue());
		return result;
	}

	private OtherPharmaco mapToOtherPharmaco(Integer indicationId, SnomedBo snomed, Integer dosageId) {
		OtherPharmaco result = new OtherPharmaco();
		Integer snomedId = snomedService.getSnomedId(snomed)
				.orElseGet(() -> snomedService.createSnomedTerm(snomed));
		result.setIndicationId(indicationId);
		result.setSnomedId(snomedId);
		result.setDosageId(dosageId);
		return result;
	}

}
