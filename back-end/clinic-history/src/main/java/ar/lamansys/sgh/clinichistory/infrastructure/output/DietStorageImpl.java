package ar.lamansys.sgh.clinichistory.infrastructure.output;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import ar.lamansys.sgh.shared.infrastructure.input.service.HospitalUserPersonInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedHospitalUserPort;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;

import org.springframework.stereotype.Service;
import ar.lamansys.sgh.clinichistory.application.ports.DietStorage;
import ar.lamansys.sgh.clinichistory.domain.ips.DietBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.DietRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.indication.Diet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DietStorageImpl implements DietStorage {

	private final DietRepository repository;

	private final SharedHospitalUserPort sharedHospitalUserPort;

	private final FeatureFlagsService featureFlagsService;


	@Override
	public List<DietBo> getInternmentEpisodeDiets(Integer internmentEpisodeId) {
		log.debug("Input parameter -> internmentEpisodeId {}", internmentEpisodeId);
		List<DietBo> result = repository.getByInternmentEpisodeId(internmentEpisodeId)
				.stream()
				.map(this::mapToBo)
				.collect(Collectors.toList());
		result.forEach(dietBo -> {
			HospitalUserPersonInfoDto p = sharedHospitalUserPort.getUserCompleteInfo(dietBo.getCreatedBy());
			if(featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS) &&p.getNameSelfDetermination() != null)
				dietBo.setCreatedByName(p.getNameSelfDetermination() + " " + p.getLastName());
			else
				dietBo.setCreatedByName(p.getFirstName() + " " + p.getLastName());
		});
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public Integer createDiet(DietBo dietBo) {
		log.debug("Input parameter -> dietBo {}", dietBo);
		Integer result = repository.save(mapToEntity(dietBo)).getId();
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public Optional<DietBo> findById(Integer id) {
		log.debug("Input parameter -> id {}", id);
		Optional<DietBo> result = repository.findById(id).map(this::mapToBo);
		HospitalUserPersonInfoDto p = sharedHospitalUserPort.getUserCompleteInfo(result.get().getCreatedBy());
		if(featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS) && p.getNameSelfDetermination() != null)
			result.get().setCreatedByName(p.getNameSelfDetermination() + " " + p.getLastName());
		else
			result.get().setCreatedByName(p.getFirstName() + " " + p.getLastName());
		log.debug("Output -> {}", result.toString());
		return result;
	}

	private Diet mapToEntity(DietBo bo) {
		Diet result = new Diet();
		result.setId(bo.getId());
		result.setPatientId(bo.getPatientId());
		result.setTypeId(bo.getTypeId());
		result.setStatusId(bo.getStatusId());
		result.setIndicationDate(bo.getIndicationDate());
		result.setProfessionalId(bo.getProfessionalId());
		result.setDescription(bo.getDescription());
		return result;
	}

	private DietBo mapToBo(Diet entity){
		return new DietBo(entity.getId(),
				entity.getPatientId(),
				entity.getTypeId(),
				entity.getStatusId(),
				entity.getCreatedBy(),
				entity.getProfessionalId(),
				entity.getIndicationDate(),
				entity.getCreatedOn(),
				entity.getDescription());
	}
	
}