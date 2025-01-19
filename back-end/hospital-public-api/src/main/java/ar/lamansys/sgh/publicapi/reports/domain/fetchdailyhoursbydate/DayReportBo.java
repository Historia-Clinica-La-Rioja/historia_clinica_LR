package ar.lamansys.sgh.publicapi.reports.domain.fetchdailyhoursbydate;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DayReportBo {
	private LocalDate day;
	private List<DailyHoursBo> dailyHours;
}
