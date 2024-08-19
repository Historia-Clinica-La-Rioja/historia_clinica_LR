package ar.lamansys.sgx.shared.dates.utils;

import static org.assertj.core.api.Assertions.assertThat;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import ar.lamansys.sgx.shared.dates.controller.dto.TimeDto;

import org.junit.jupiter.api.Test;

public class DateTimeDtoTest {

	@Test
	void compareTo_differentDatesSameTimes() {
		DateDto date1 = new DateDto(2023, 1, 1);
		DateDto date2 = new DateDto(2024, 6, 4);
		TimeDto time = new TimeDto(12, 0, 0);

		DateTimeDto dateTime1 = new DateTimeDto(date1, time);
		DateTimeDto dateTime2 = new DateTimeDto(date2, time);

		assertThat(dateTime1.compareTo(dateTime2)).isLessThan(0);
		assertThat(dateTime2.compareTo(dateTime1)).isGreaterThan(0);
	}

	@Test
	void compareTo_sameDatesDifferentTimes() {
		DateDto date = new DateDto(2024, 6, 4);
		TimeDto time1 = new TimeDto(12, 15, 30);
		TimeDto time2 = new TimeDto(13, 0, 0);

		DateTimeDto dateTime1 = new DateTimeDto(date, time1);
		DateTimeDto dateTime2 = new DateTimeDto(date, time2);

		assertThat(dateTime1.compareTo(dateTime2)).isLessThan(0);
		assertThat(dateTime2.compareTo(dateTime1)).isGreaterThan(0);
	}

	@Test
	void compareTo_sameDatesAndTimes() {
		DateDto date = new DateDto(2024, 1, 1);
		TimeDto time = new TimeDto(12, 0, 0);

		DateTimeDto dateTime1 = new DateTimeDto(date, time);
		DateTimeDto dateTime2 = new DateTimeDto(date, time);

		assertThat(dateTime1.compareTo(dateTime2)).isEqualTo(0);
	}

	@Test
	void compareTo_differentDatesAndTimes() {
		DateDto date1 = new DateDto(2023, 1, 1);
		DateDto date2 = new DateDto(2024, 1, 1);
		TimeDto time1 = new TimeDto(12, 0, 0);
		TimeDto time2 = new TimeDto(13, 0, 0);

		DateTimeDto dateTime1 = new DateTimeDto(date1, time1);
		DateTimeDto dateTime2 = new DateTimeDto(date2, time2);

		assertThat(dateTime1.compareTo(dateTime2)).isLessThan(0);
		assertThat(dateTime2.compareTo(dateTime1)).isGreaterThan(0);
	}

	@Test
	void compareTo_nullSeconds() {
		DateDto date = new DateDto(2024, 6, 4);
		TimeDto time1 = new TimeDto(12, 0, null);
		TimeDto time2 = new TimeDto(12, 0, 0);

		DateTimeDto dateTime1 = new DateTimeDto(date, time1);
		DateTimeDto dateTime2 = new DateTimeDto(date, time2);

		assertThat(dateTime1.compareTo(dateTime2)).isEqualTo(0);
	}

}
