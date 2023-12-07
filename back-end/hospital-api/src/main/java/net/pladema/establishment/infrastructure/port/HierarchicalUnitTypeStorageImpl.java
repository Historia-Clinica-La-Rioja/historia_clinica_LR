package net.pladema.establishment.infrastructure.port;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.establishment.application.port.HierarchicalUnitTypeStorage;

import net.pladema.establishment.domain.hierarchicalunits.HierarchicalUnitTypeBo;

import net.pladema.establishment.repository.HierarchicalUnitTypeRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class HierarchicalUnitTypeStorageImpl implements HierarchicalUnitTypeStorage {

	private final HierarchicalUnitTypeRepository hierarchicalUnitTypeRepository;

	@Override
	public List<HierarchicalUnitTypeBo> fetchAllByInstitutionId(Integer institutionId) {
		log.debug("Fetch all hierarchical unit types in institutionId {} ", institutionId);
		return hierarchicalUnitTypeRepository.getAllByInstitutionId(institutionId);
	}

}
