package ar.lamansys.online.domain.diary;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class OpeningHoursBasicDataBo {

	private Integer id;

	private LocalTime from;

	private LocalTime to;

}
