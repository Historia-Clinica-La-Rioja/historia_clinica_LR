package net.pladema.medicalconsultation.equipmentdiary.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
@Getter
@Setter
@ToString
public class EquipmentDiaryDto extends EquipmentDiaryADto{

	@NotNull
	private Integer id;
}
