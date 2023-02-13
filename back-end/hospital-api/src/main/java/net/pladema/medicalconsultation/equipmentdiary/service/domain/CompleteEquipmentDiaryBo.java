package net.pladema.medicalconsultation.equipmentdiary.service.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CompleteEquipmentDiaryBo extends EquipmentDiaryBo {

	private Integer sectorId;

	private String sectorDescription;

	public CompleteEquipmentDiaryBo(EquipmentDiaryBo equipmentDiaryBo) {
		appointmentDuration = equipmentDiaryBo.getAppointmentDuration();
		id = equipmentDiaryBo.getId();
		startDate = equipmentDiaryBo.getStartDate();
		endDate = equipmentDiaryBo.getEndDate();
		automaticRenewal = equipmentDiaryBo.isAutomaticRenewal();
		includeHoliday = equipmentDiaryBo.isIncludeHoliday();
		equipmentId = equipmentDiaryBo.getEquipmentId();
	}

}
