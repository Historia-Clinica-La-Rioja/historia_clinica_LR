package ar.lamansys.sgh.publicapi.reports.domain;

import java.time.LocalDate;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ConsultationItemWithDateBo {
	private String sctId;
	private String pt;
	private String cie10Id;
	private LocalDate startDate;
	private LocalDate endDate;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ConsultationItemWithDateBo)) return false;
		ConsultationItemWithDateBo that = (ConsultationItemWithDateBo) o;
		return Objects.equals(getSctId(), that.getSctId()) && Objects.equals(getPt(), that.getPt()) && Objects.equals(getCie10Id(), that.getCie10Id()) && Objects.equals(getStartDate(), that.getStartDate()) && Objects.equals(getEndDate(), that.getEndDate());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getSctId(), getPt(), getCie10Id(), getStartDate(), getEndDate());
	}
}
