package net.pladema.establishment.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class HolidayVo implements Serializable {

	private LocalDate date;
	private String description;

}
