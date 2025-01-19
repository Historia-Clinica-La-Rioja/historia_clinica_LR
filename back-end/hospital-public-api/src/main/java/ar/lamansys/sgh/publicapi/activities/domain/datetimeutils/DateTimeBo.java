package ar.lamansys.sgh.publicapi.activities.domain.datetimeutils;


import lombok.Getter;

@Getter
public class DateTimeBo {

	private final DateBo date;

	private final TimeBo time;

	public DateTimeBo(DateBo date, TimeBo time) {
		this.date = date;
		this.time = time;
	}

}
