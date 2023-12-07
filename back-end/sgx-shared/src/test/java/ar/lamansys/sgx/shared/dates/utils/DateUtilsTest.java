package ar.lamansys.sgx.shared.dates.utils;

import static ar.lamansys.sgx.shared.dates.utils.DateUtils.fromStringToLocalDate;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import ar.lamansys.sgx.shared.dates.exceptions.DateParseException;

class DateUtilsTest {

	@Test
	void fromStringToLocalDate_invalidValues() throws DateParseException {
		assertThat(fromStringToLocalDate(null))
				.isNull();

		fromStringToLocalDateException("", "Text '' could not be parsed at index 0");
		fromStringToLocalDateException("2022-9-0", "Text '2022-9-0' could not be parsed at index 5");
		fromStringToLocalDateException("2022/09/07", "Text '2022/09/07' could not be parsed at index 4");
		fromStringToLocalDateException("2022-09-07T", "Text '2022-09-07T' could not be parsed, unparsed text found at index 10");
	}

	@Test
	void fromStringToLocalDate_validValues() throws DateParseException {
		assertThat(fromStringToLocalDate(null))
				.isNull();

		Assertions.assertThat(
				fromStringToLocalDate("2022-09-31")
		).isEqualTo(LocalDate.of(2022, 9, 30));

		Assertions.assertThat(
				fromStringToLocalDate("2022-01-31")
		).isEqualTo(LocalDate.of(2022, 1, 31));
		Assertions.assertThat(
				fromStringToLocalDate("2022-12-31")
		).isEqualTo(LocalDate.of(2022, 12, 31));
		Assertions.assertThat(
				fromStringToLocalDate("2022-02-31")
		).isEqualTo(LocalDate.of(2022, 2, 28));
	}


	private static void fromStringToLocalDateException(String value, String message) {
		DateParseException exception = Assertions.catchThrowableOfType(() -> {
			fromStringToLocalDate(value);
		}, DateParseException.class);

		Assertions.assertThat(exception).isNotNull();
		Assertions.assertThat(exception.originalDateValue).isEqualTo(value);
		Assertions.assertThat(exception.getMessage()).isEqualTo(message);
	}

}