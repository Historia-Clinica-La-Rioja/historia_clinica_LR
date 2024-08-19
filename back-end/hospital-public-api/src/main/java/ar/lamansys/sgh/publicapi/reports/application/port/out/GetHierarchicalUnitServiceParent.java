package ar.lamansys.sgh.publicapi.reports.application.port.out;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.reports.domain.HierarchicalUnitBo;
import ar.lamansys.sgh.publicapi.reports.domain.fetchconsultationsbydate.HierarchicalParentChildBo;

@Service
public class GetHierarchicalUnitServiceParent {

	/* Esta es una clase utilitaria temporal que debería desaparecer
	cuando se agregue la columna con el padre de tipo servicio más cercano
	a la tabla unidad jerárquica */

	private static final Short SERVICE_TYPE = 8;

	private final List<Object[]> serviceParents;

	private static final String queryForParents = "select hierarchical_unit_parent_id as parent, hierarchical_unit_child_id as child, " +
			"hu.type_id as parent_type, hu2.type_id as child_type, " +
			"hu.alias as parent_alias, hu2.alias as child_alias " +
			"from hierarchical_unit_relationship hur join hierarchical_unit hu " +
			"on hu.id = hur.hierarchical_unit_parent_id " +
			"join hierarchical_unit hu2 on hu2.id = hur.hierarchical_unit_child_id " +
			"where hur.deleted IS NOT TRUE AND hu.deleted IS NOT TRUE AND hu2.deleted IS NOT TRUE ";


	public GetHierarchicalUnitServiceParent(EntityManager entityManager){
		this.serviceParents = entityManager.createNativeQuery(queryForParents)
				.getResultList();
	}

	public Optional<HierarchicalUnitBo> getServiceParent(HierarchicalUnitBo serviceHierarchicalUnit) {

		if(serviceHierarchicalUnit.getId() == null || serviceHierarchicalUnit.getType() == null)
			return Optional.empty();

		if(serviceHierarchicalUnit.getType().equals(SERVICE_TYPE))
			return Optional.of(new HierarchicalUnitBo(serviceHierarchicalUnit.getId(), serviceHierarchicalUnit.getDescription(), serviceHierarchicalUnit.getType()));

		//a[0] will hold the child original hierarchical unit
		var parentsAndChildren = serviceParents.stream()
				.map(row -> new HierarchicalParentChildBo(
						(Integer) row[0],
						(Integer) row[1],
						((Integer) row[2]).shortValue(),
						((Integer) row[3]).shortValue(),
						(String) row[4],
						(String) row[5]
				))
				.collect(Collectors.toList());

		var actual = serviceHierarchicalUnit;
		boolean end = false;

		while(!end) {
			var a = findParent(parentsAndChildren, actual.getId());
			if(a == null) {
				end = true;
				actual = null;
			}
			else {
				if(a.getParentType().equals(SERVICE_TYPE)) {
					end = true;
				}
				actual = new HierarchicalUnitBo(a.getParentId(), a.getParentAlias(), a.getParentType());
			}
		}
		//the type is not needed in the final return
		return actual == null ? Optional.empty() : Optional.of(new HierarchicalUnitBo(actual.getId(), actual.getDescription(), actual.getType()));
	}

	private HierarchicalParentChildBo findParent(List<HierarchicalParentChildBo> serviceParents, Integer childId) {
		var results = serviceParents.stream()
				.filter(a -> a.getChildId().equals(childId))
				.collect(Collectors.toList());

		if(results.isEmpty()) {
			return null;
		}

		return results.stream()
				.filter(a -> a.getParentType().equals(SERVICE_TYPE))
				.findFirst()
				.orElse(results.get(0));
	}
}
