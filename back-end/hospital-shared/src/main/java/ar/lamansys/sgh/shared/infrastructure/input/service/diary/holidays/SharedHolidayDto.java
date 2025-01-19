package ar.lamansys.sgh.shared.infrastructure.input.service.diary.holidays;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SharedHolidayDto {
	private DateDto date;
	private String description;
}
