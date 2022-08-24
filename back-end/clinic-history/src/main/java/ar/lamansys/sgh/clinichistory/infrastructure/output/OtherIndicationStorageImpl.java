package ar.lamansys.sgh.clinichistory.infrastructure.output;

import ar.lamansys.sgh.clinichistory.application.ports.OtherIndicationStorage;
import ar.lamansys.sgh.clinichistory.domain.ips.DosageBo;
import ar.lamansys.sgh.clinichistory.domain.ips.EUnitsOfTimeBo;
import ar.lamansys.sgh.clinichistory.domain.ips.OtherIndicationBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.EDocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.DosageRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.OtherIndicationRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.Dosage;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.indication.OtherIndication;
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
public class OtherIndicationStorageImpl implements OtherIndicationStorage {

	private final OtherIndicationRepository otherIndicationRepository;

	private final SharedHospitalUserPort sharedHospitalUserPort;

	private final DosageRepository dosageRepository;

	private final FeatureFlagsService featureFlagsService;


	@Override
	public List<OtherIndicationBo> getInternmentEpisodeOtherIndications(Integer internmentEpisodeId) {
		log.debug("Input parameter -> internmentEpisodeId {}", internmentEpisodeId);
		List<OtherIndicationBo> result = otherIndicationRepository.getByInternmentEpisodeId(internmentEpisodeId, EDocumentType.INDICATION.getId())
				.stream()
				.map(entity -> {
					OtherIndicationBo oiBo = mapToBo(entity);
					HospitalUserPersonInfoDto p = sharedHospitalUserPort.getUserCompleteInfo(oiBo.getCreatedBy());
					if(featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS) && p.getNameSelfDetermination() != null)
						oiBo.setCreatedByName(p.getNameSelfDetermination() + " " + p.getLastName());
					else
						oiBo.setCreatedByName(p.getFirstName() + " " + p.getLastName());
					DosageBo  dosageBo = dosageRepository.findById(entity.getDosageId()).
							map(this::mapToDosageBo).orElse(null);
					oiBo.setDosage(dosageBo);
					return oiBo;})
				.collect(Collectors.toList());
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public Integer createOtherIndication(OtherIndicationBo otherIndicationBo) {
		log.debug("Input parameter -> otherIndicationBo {}", otherIndicationBo);
		Integer dosageId = dosageRepository.save(mapToDosage(otherIndicationBo.getDosage())).getId();
		otherIndicationBo.getDosage().setId(dosageId);
		Integer result = otherIndicationRepository.save(mapToEntity(otherIndicationBo)).getId();
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public Optional<OtherIndicationBo> findById(Integer id) {
		log.debug("Input parameter -> id {}", id);
		Optional<OtherIndicationBo> result = otherIndicationRepository.findById(id).map(entity -> {
					OtherIndicationBo oiBo = mapToBo(entity);
					HospitalUserPersonInfoDto p = sharedHospitalUserPort.getUserCompleteInfo(oiBo.getCreatedBy());
					if (featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS) && p.getNameSelfDetermination() != null)
						oiBo.setCreatedByName(p.getNameSelfDetermination() + " " + p.getLastName());
					else oiBo.setCreatedByName(p.getFirstName() + " " + p.getLastName());
					DosageBo dosageBo = dosageRepository.findById(entity.getDosageId()).map(this::mapToDosageBo).orElse(null);
					oiBo.setDosage(dosageBo);
					log.debug("Output -> {}", oiBo.toString());
					return oiBo;
				});
		log.debug("Output -> {}", result.toString());
		return result;
	}

	private Dosage mapToDosage(DosageBo bo){
		Dosage result = new Dosage();
		result.setFrequency(bo.getFrequency());
		result.setPeriodUnit(bo.getPeriodUnit());
		result.setStartDate(bo.getStartDate());
		result.setEndDate(bo.getEndDate());
		result.setEvent(bo.getEvent());
		return result;
	}

	private DosageBo mapToDosageBo(Dosage entity) {
		DosageBo result = new DosageBo();
		result.setId(entity.getId());
		result.setFrequency(entity.getFrequency());
		if (entity.getPeriodUnit() != null)
			result.setPeriodUnit(EUnitsOfTimeBo.map(entity.getPeriodUnit()));
		result.setStartDate(entity.getStartDate());
		result.setEndDate(entity.getEndDate());
		result.setEvent(entity.getEvent());
		return result;
	}

	private OtherIndication mapToEntity(OtherIndicationBo bo) {
		OtherIndication result = new OtherIndication();
		result.setId(bo.getId());
		result.setPatientId(bo.getPatientId());
		result.setTypeId(bo.getTypeId());
		result.setStatusId(bo.getStatusId());
		result.setIndicationDate(bo.getIndicationDate());
		result.setProfessionalId(bo.getProfessionalId());
		result.setOtherIndicationType(bo.getOtherIndicationTypeId());
		result.setDosageId(bo.getDosage().getId());
		result.setDescription(bo.getDescription());
		result.setOtherType(bo.getOtherType());
		return result;
	}

	private OtherIndicationBo mapToBo(OtherIndication entity){
		return new OtherIndicationBo(entity.getId(),
				entity.getPatientId(),
				entity.getTypeId(),
				entity.getStatusId(),
				entity.getCreatedBy(),
				entity.getProfessionalId(),
				entity.getIndicationDate(),
				entity.getCreatedOn(),
				entity.getOtherIndicationType(),
				null,
				entity.getDescription(),
				entity.getOtherType());
	}

}