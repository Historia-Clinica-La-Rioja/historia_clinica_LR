package ar.lamansys.sgh.publicapi.activities.domain.datetimeutils;

import lombok.Getter;
import java.util.Objects;

@Getter
public class DateBo {
	private final Integer year;

	private final Integer month;

	private final Integer day;


	public DateBo(Integer year, Integer month, Integer day) {
		this.year = year;
		this.month = month;
		this.day = day;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof DateBo)) return false;
		DateBo dateBo = (DateBo) o;
		return Objects.equals(getYear(), dateBo.getYear()) && Objects.equals(getMonth(), dateBo.getMonth()) && Objects.equals(getDay(), dateBo.getDay());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getYear(), getMonth(), getDay());
	}

}
