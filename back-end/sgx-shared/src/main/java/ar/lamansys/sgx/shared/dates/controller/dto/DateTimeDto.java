package ar.lamansys.sgx.shared.dates.controller.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
public class DateTimeDto implements Comparable<DateTimeDto>{

    private final DateDto date;

    private final TimeDto time;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public DateTimeDto(@JsonProperty(value = "date") DateDto date,
                       @JsonProperty(value = "time") TimeDto time) {
        this.date = date;
        this.time = time;
    }

	@Override
	public int compareTo(DateTimeDto dateTimeDto) {
		int dateComparisonResult = dateComparator(dateTimeDto.date);
		if (dateComparisonResult != 0) {
			return dateComparisonResult;
		}
		return timeComparator(dateTimeDto.time);
	}

	private int dateComparator(DateDto date) {
		if (!Objects.equals(this.date.getYear(), date.getYear())) {
			return Integer.compare(this.date.getYear(), date.getYear());
		} else if (!Objects.equals(this.date.getMonth(), date.getMonth())) {
			return Integer.compare(this.date.getMonth(), date.getMonth());
		} else {
			return Integer.compare(this.date.getDay(), date.getDay());
		}
	}

	private int timeComparator(TimeDto time) {
		if (!Objects.equals(this.time.getHours(), time.getHours())) {
			return Integer.compare(this.time.getHours(), time.getHours());
		} else if (!Objects.equals(this.time.getMinutes(), time.getMinutes())) {
			return Integer.compare(this.time.getMinutes(), time.getMinutes());
		} else if (this.time.getSeconds() != null) {
			return Integer.compare(this.time.getSeconds(), time.getSeconds());
		}
		return 0;
	}

	@Override
	public String toString() {
		return String.format("%sT%s", date, time);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DateTimeDto that = (DateTimeDto) o;
		return Objects.equals(date, that.date) && Objects.equals(time, that.time);
	}

	@Override
	public int hashCode() {
		return Objects.hash(date, time);
	}
}
