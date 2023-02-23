package net.pladema.medicalconsultation.equipmentdiary.controller.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CompleteEquipmentDiaryDto extends EquipmentDiaryDto {

    private Integer sectorId;

    private String sectorDescription;

}
