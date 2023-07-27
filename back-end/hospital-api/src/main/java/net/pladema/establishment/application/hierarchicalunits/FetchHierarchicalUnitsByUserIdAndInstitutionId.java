package net.pladema.establishment.application.hierarchicalunits;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.establishment.application.port.HierarchicalUnitStorage;

import net.pladema.establishment.service.domain.HierarchicalUnitBo;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FetchHierarchicalUnitsByUserIdAndInstitutionId {

	private final HierarchicalUnitStorage hierarchicalUnitStorage;

	public List<HierarchicalUnitBo> run(Integer userId, Integer institutionId) {
		log.debug("Input parameters -> userId {}, institutionId {}", userId, institutionId);
		return hierarchicalUnitStorage.fetchsHierarchicalUnistByUserIdAndInstitutionId(userId, institutionId);
	}

}
