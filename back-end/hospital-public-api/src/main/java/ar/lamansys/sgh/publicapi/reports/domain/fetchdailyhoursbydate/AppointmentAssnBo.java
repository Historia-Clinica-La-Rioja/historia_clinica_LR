package ar.lamansys.sgh.publicapi.reports.domain.fetchdailyhoursbydate;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AppointmentAssnBo {
	private Integer appointmentId;
	private Integer diaryId;
	private Integer openingHoursId;
	private String blockReason;
}
