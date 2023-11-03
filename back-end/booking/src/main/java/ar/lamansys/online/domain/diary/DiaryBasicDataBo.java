package ar.lamansys.online.domain.diary;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DiaryBasicDataBo {

	private LocalDate startDate;

	private LocalDate endDate;

	private List<OpeningHoursBasicDataBo> openingHours;

	public boolean isEmpty() {
		return startDate == null || endDate == null;
	}

}
