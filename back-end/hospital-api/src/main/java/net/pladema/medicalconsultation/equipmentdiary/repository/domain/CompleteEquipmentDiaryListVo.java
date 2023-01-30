package net.pladema.medicalconsultation.equipmentdiary.repository.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.medicalconsultation.equipmentdiary.repository.entity.EquipmentDiary;

@ToString
@Getter
@Setter
@EqualsAndHashCode(callSuper=false)
public class CompleteEquipmentDiaryListVo extends EquipmentDiaryListVo {

	private final Integer sectorId;

	private final String sectorDescription;

	public CompleteEquipmentDiaryListVo(EquipmentDiary equipmentDiary, Integer sectorId,
										String sectorDescription) {
		super(equipmentDiary);
		this.sectorId = sectorId;
		this.sectorDescription = sectorDescription;
	}





}
