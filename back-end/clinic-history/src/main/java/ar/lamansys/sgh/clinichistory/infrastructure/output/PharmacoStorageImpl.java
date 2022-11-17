package ar.lamansys.sgh.clinichistory.infrastructure.output;

import ar.lamansys.sgh.clinichistory.application.ports.PharmacoStorage;
import ar.lamansys.sgh.clinichistory.domain.ips.EUnitsOfTimeBo;
import ar.lamansys.sgh.clinichistory.domain.ips.EVia;
import ar.lamansys.sgh.clinichistory.domain.ips.PharmacoSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.ips.QuantityBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DosageBo;
import ar.lamansys.sgh.clinichistory.domain.ips.OtherPharmacoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.PharmacoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.domain.ips.services.SnomedService;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentIndicationRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.EDocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.DosageRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.OtherPharmacoRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.PharmacoRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.QuantityRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.Dosage;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.Quantity;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.indication.OtherPharmaco;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.indication.Pharmaco;
import ar.lamansys.sgh.shared.infrastructure.input.service.HospitalUserPersonInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedHospitalUserPort;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PharmacoStorageImpl implements PharmacoStorage {

	private final QuantityRepository quantityRepository;
	private final DosageRepository dosageRepository;
	private final PharmacoRepository pharmacoRepository;
	private final OtherPharmacoRepository otherPharmacoRepository;
	private final DocumentIndicationRepository documentIndicationRepository;
	private final SnomedService snomedService;
	private final SharedHospitalUserPort sharedHospitalUserPort;
	private final FeatureFlagsService featureFlagsService;

	@Override
	public Integer createPharmaco(PharmacoBo pharmacoBo) {
		log.debug("Input parameter -> pharmacoBo {}", pharmacoBo);
		Long quantity = quantityRepository.save(mapToQuantity(pharmacoBo.getDosage().getQuantity())).getId();
		Integer dosageId = dosageRepository.save(mapToDosage(pharmacoBo.getDosage(), quantity)).getId();
		pharmacoBo.getDosage().setId(dosageId);
		Integer pharmacoId = pharmacoRepository.save(mapToEntity(pharmacoBo)).getId();
		if (pharmacoBo.getSolvent() != null) {
			Integer solventId = saveSolvent(pharmacoBo.getSolvent(), pharmacoId);
			log.debug("pharmacoId {}, solventId {} -> ", pharmacoId, solventId);
		}
		log.debug("Output -> {}", pharmacoId);
		return pharmacoId;
	}

	@Override
	public List<PharmacoSummaryBo> getInternmentEpisodePharmacos(Integer internmentEpisodeId) {
		log.debug("Input parameter -> internmentEpisodeId {}", internmentEpisodeId);
		List<PharmacoSummaryBo> result = pharmacoRepository.getByInternmentEpisodeId(internmentEpisodeId, EDocumentType.INDICATION.getId())
				.stream()
				.map(this::mapToPharmacoSummaryBo)
				.collect(Collectors.toList());
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public Optional<PharmacoBo> findById(Integer id) {
		log.debug("Input parameter -> id {}", id);
		Optional<PharmacoBo> result = pharmacoRepository.findById(id).map(this::mapToBo);
		log.debug("Output -> {}", result.toString());
		return result;
	}

	private PharmacoBo mapToBo(Pharmaco entity) {
		PharmacoBo result = new PharmacoBo();
		HospitalUserPersonInfoDto p = sharedHospitalUserPort.getUserCompleteInfo(entity.getCreatedBy());
		SnomedBo snomedBo = snomedService.getSnomed(entity.getSnomedId());
		DosageBo dosageBo = dosageRepository.findById(entity.getDosageId())
				.map(this::mapToDosageBo).orElse(null);
		result.setId(entity.getId());
		result.setPatientId(entity.getPatientId());
		result.setStatusId(entity.getStatusId());
		result.setTypeId(entity.getTypeId());
		if(featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS) && p.getNameSelfDetermination() != null)
			result.setCreatedByName(p.getNameSelfDetermination() + " " + p.getLastName());
		else
			result.setCreatedByName(p.getFirstName() + " " + p.getLastName());
		result.setIndicationDate(entity.getIndicationDate());
		result.setCreatedOn(entity.getCreatedOn());
		result.setSnomed(snomedBo);
		result.setDosage(dosageBo);
		result.setViaId(entity.getViaId().intValue());
		result.setFoodRelationId(entity.getFoodRelationId().intValue());
		result.setHealthConditionId(entity.getHealthConditionId());
		result.setPatientProvided(entity.getPatientProvided());
		result.setCreatedBy(entity.getCreatedBy());
		result.setProfessionalId(entity.getProfessionalId());
		result.setSolvent(getSolvent(entity.getId()));
		result.setNote(documentIndicationRepository.getNote(entity.getId()));
		return result;
	}

	private OtherPharmacoBo getSolvent(Integer id){
		List<OtherPharmacoBo> otherPharmacos = getOtherPharmacosByIndicationId(id);
		if (otherPharmacos.isEmpty())
			return null;
		return otherPharmacos.get(0);
	}

	private List<OtherPharmacoBo> getOtherPharmacosByIndicationId(Integer id){
		List<OtherPharmacoBo> result = otherPharmacoRepository.getByIndicationId(id)
				.stream()
				.map(entity -> {
					OtherPharmacoBo opBo = mapToOtherPharmacoBo(entity);
					return opBo;})
				.collect(Collectors.toList());
		return result;
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

	private OtherPharmacoBo mapToOtherPharmacoBo(OtherPharmaco entity){
		SnomedBo snomedBo = snomedService.getSnomed(entity.getSnomedId());
		Optional<Dosage> dosage = dosageRepository.findById(entity.getDosageId());
		OtherPharmacoBo result = new OtherPharmacoBo();
		result.setIndicationId(entity.getIndicationId());
		result.setSnomed(snomedBo);
		result.setDosage(dosage.isPresent() ? mapToDosageBo(dosage.get()) : null);
		return result;
	}

	private PharmacoSummaryBo mapToPharmacoSummaryBo(Pharmaco entity){
		PharmacoSummaryBo result = new PharmacoSummaryBo();
		HospitalUserPersonInfoDto p = sharedHospitalUserPort.getUserCompleteInfo(entity.getCreatedBy());
		SnomedBo snomedBo = snomedService.getSnomed(entity.getSnomedId());
		DosageBo dosageBo = dosageRepository.findById(entity.getDosageId())
				.map(this::mapToDosageBo).orElse(null);
		result.setId(entity.getId());
		result.setPatientId(entity.getPatientId());
		result.setStatusId(entity.getStatusId());
		result.setTypeId(entity.getTypeId());
		if(featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS) && p.getNameSelfDetermination() != null)
			result.setCreatedByName(p.getNameSelfDetermination() + " " + p.getLastName());
		else
			result.setCreatedByName(p.getFirstName() + " " + p.getLastName());
		result.setIndicationDate(entity.getIndicationDate());
		result.setCreatedOn(entity.getCreatedOn());
		result.setSnomed(snomedBo);
		result.setDosage(dosageBo);
		result.setVia(EVia.getById(entity.getViaId()).getDescription());
		return result;
	}

	private DosageBo mapToDosageBo(Dosage entity) {
		DosageBo result = new DosageBo();
		result.setId(entity.getId());
		result.setFrequency(entity.getFrequency());
		result.setPeriodUnit(EUnitsOfTimeBo.map(entity.getPeriodUnit()));
		result.setStartDate(entity.getStartDate());
		result.setEndDate(entity.getEndDate());
		result.setEvent(entity.getEvent());
		quantityRepository.findById(entity.getDoseQuantityId()).ifPresent( quantity ->
				result.setQuantity(new QuantityBo(quantity.getValue().intValue(), quantity.getUnit())));
		return result;
	}

}
