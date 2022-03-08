package ar.lamansys.sgh.clinichistory.infrastructure.output;

import java.util.List;
import java.util.stream.Collectors;

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


	@Override
	public List<DietBo> getInternmentEpisodeDiets(Integer internmentEpisodeId) {
		log.debug("Input parameter -> internmentEpisodeId {}", internmentEpisodeId);
		List<DietBo> result = repository.getByInternmentEpisodeId(internmentEpisodeId)
				.stream()
				.map(this::mapToBo)
				.collect(Collectors.toList());
		log.debug("Output -> {}", result);
		return result;
	}

	private DietBo mapToBo(Diet entity){
		return new DietBo(entity.getId(),
				entity.getPatientId(),
				entity.getTypeId(),
				entity.getStatusId(),
				entity.getCreatedBy(),
				entity.getIndicationDate(),
				entity.getDescription());
	}

}