package ar.lamansys.sgh.publicapi.reports.domain.fetchconsultationsbydate;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@AllArgsConstructor
@Getter
public class HierarchicalParentChildBo {
	Integer parentId;
	Integer childId;
	Short parentType;
	Short childType;
	String parentAlias;
	String childAlias;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof HierarchicalParentChildBo)) return false;
		HierarchicalParentChildBo that = (HierarchicalParentChildBo) o;
		return Objects.equals(getParentId(), that.getParentId()) && Objects.equals(getChildId(), that.getChildId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getParentId(), getChildId());
	}
}
