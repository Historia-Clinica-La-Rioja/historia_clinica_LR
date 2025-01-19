package net.pladema.medicalconsultation.equipmentdiary.service.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"diaryId", "overturnCount"})
@ToString
@NoArgsConstructor
public class EquipmentDiaryOpeningHoursBo {

    private Integer diaryId;

    private EquipmentOpeningHoursBo openingHours;

    private Short medicalAttentionTypeId;

    private Short overturnCount = 0;

    private Boolean externalAppointmentsAllowed;

	public boolean overlap(EquipmentDiaryOpeningHoursBo current) {
		return openingHours.overlap(current.getOpeningHours());
	}

}
