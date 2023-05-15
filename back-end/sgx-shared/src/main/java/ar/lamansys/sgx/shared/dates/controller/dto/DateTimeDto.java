package ar.lamansys.sgx.shared.dates.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
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
		Integer dateComparisonResult = dateComparator(dateTimeDto.date);
		Integer timeComparisonResult = timeComparator(dateTimeDto.time);
		if (dateComparisonResult.equals(1) || dateComparisonResult.equals(-1))
			return dateComparisonResult;
		if (timeComparisonResult.equals(1) || timeComparisonResult.equals(-1))
			return timeComparisonResult;
		return dateComparisonResult;
	}

	private Integer dateComparator(DateDto date) {
		if (this.date.getYear() < date.getYear()) return -1;
		if (this.date.getMonth() < date.getMonth()) return -1;
		if (this.date.getDay() < date.getDay()) return -1;
		if (this.date.getYear() > date.getYear()) return 1;
		if (this.date.getMonth() > date.getMonth()) return 1;
		if (this.date.getDay() > date.getDay()) return 1;
		return 0;
	}

	private Integer timeComparator(TimeDto time) {
		if (this.time.getHours() < time.getHours()) return -1;
		if (this.time.getMinutes() < time.getMinutes()) return -1;
		if (this.time.getSeconds() < time.getSeconds()) return -1;
		if (this.time.getHours() > time.getHours()) return 1;
		if (this.time.getMinutes() > time.getMinutes()) return 1;
		if (this.time.getSeconds() > time.getSeconds()) return 1;
		return 0;
	}

}
