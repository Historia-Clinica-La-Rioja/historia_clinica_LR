package net.pladema.medicalconsultation.equipmentdiary.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
public class UpdateEquipmentDiaryAppointmentBo {

	private Integer id;

	private LocalDate date;

	private LocalTime time;

	private Short stateId;

	private Short medicalAttentionTypeId;

}
