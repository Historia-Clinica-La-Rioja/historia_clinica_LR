package net.pladema.establishment.application.hierarchicalunits;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.establishment.application.port.HierarchicalUnitStorage;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FetchDescendantsByHierarchicalUnitId {

	private final HierarchicalUnitStorage hierarchicalUnitStorage;

	public List<Integer> run(Integer hierarchicalUnitId) {
		log.debug("Input parameters -> hierarchicalUnitId {}", hierarchicalUnitId);
		return hierarchicalUnitStorage.fetchAllDescendantIdsByHierarchicalUnitId(List.of(hierarchicalUnitId));
	}
}
