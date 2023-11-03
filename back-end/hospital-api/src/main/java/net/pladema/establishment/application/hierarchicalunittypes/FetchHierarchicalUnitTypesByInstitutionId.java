package net.pladema.establishment.application.hierarchicalunittypes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.establishment.application.port.HierarchicalUnitTypeStorage;

import net.pladema.establishment.domain.hierarchicalunits.HierarchicalUnitTypeBo;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FetchHierarchicalUnitTypesByInstitutionId {

	private final HierarchicalUnitTypeStorage hierarchicalUnitTypeStorage;

	public List<HierarchicalUnitTypeBo> run(Integer institutionId) {
		log.debug("Input parameter -> institutionId {}", institutionId);
		return hierarchicalUnitTypeStorage.fetchAllByInstitutionId(institutionId);
	}
}