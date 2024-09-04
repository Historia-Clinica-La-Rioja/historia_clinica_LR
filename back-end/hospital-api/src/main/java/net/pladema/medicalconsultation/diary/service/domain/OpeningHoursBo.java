package net.pladema.medicalconsultation.diary.service.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.medicalconsultation.diary.repository.entity.OpeningHours;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true, exclude = "id")
@NoArgsConstructor
public class OpeningHoursBo extends TimeRangeBo {

    private Integer id;

    private Short dayWeekId;

    public OpeningHoursBo(OpeningHours openingHours){
        super(openingHours.getFrom(), openingHours.getTo());
        this.dayWeekId = openingHours.getDayWeekId();
        this.id = openingHours.getId();
    }

	public OpeningHoursBo(Short dayWeekId, TimeRangeBo timeRange) {
		super(timeRange.getFrom(), timeRange.getTo());
		this.dayWeekId = dayWeekId;
	}

	public boolean overlap(OpeningHoursBo other) {
		return getDayWeekId().equals(other.getDayWeekId())
				&& getFrom().isBefore(other.getTo())
				&& getTo().isAfter(other.getFrom());
	}

}
