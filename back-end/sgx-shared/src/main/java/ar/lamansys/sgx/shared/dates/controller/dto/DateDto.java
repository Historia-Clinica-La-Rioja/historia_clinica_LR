package ar.lamansys.sgx.shared.dates.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import ar.lamansys.sgx.shared.dates.controller.constraints.DateDtoValid;

import java.util.Objects;

@Getter
@DateDtoValid
public class DateDto {

	private final Integer year;

	private final Integer month;

	private final Integer day;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public DateDto(@JsonProperty(value = "year") Integer year,
				   @JsonProperty(value = "month") Integer month,
				   @JsonProperty(value = "day") Integer day) {
		this.year = year;
		this.month = month;
		this.day = day;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof DateDto)) return false;
		DateDto dateDto = (DateDto) o;
		return Objects.equals(getYear(), dateDto.getYear()) && Objects.equals(getMonth(), dateDto.getMonth()) && Objects.equals(getDay(), dateDto.getDay());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getYear(), getMonth(), getDay());
	}
}
