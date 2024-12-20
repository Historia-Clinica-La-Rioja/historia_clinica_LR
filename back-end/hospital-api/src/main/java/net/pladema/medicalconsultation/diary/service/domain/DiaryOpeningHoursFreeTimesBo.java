package net.pladema.medicalconsultation.diary.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class DiaryOpeningHoursFreeTimesBo {

	private Integer openingHoursId;

	private List<LocalTime> freeTimes;

}
