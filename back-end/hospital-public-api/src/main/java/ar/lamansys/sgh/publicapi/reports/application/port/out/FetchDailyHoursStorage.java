package ar.lamansys.sgh.publicapi.reports.application.port.out;

import java.util.List;

import ar.lamansys.sgh.publicapi.reports.domain.fetchdailyhoursbydate.DayReportBo;

public interface FetchDailyHoursStorage {

	List<DayReportBo> fetchDiaryHoursByDay(String dateFrom, String dateUntil, Integer institutionId, Integer hierarchicalUnitId);

}
