package ar.lamansys.sgh.publicapi.prescription.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@ToString
public class ChangePrescriptionStateMultipleMedicationBo {

	private Integer prescriptionLine;

	private Short prescriptionStateId;

	private List<DispensedMedicationBo> dispensedMedicationBos;

	private String observations;

}
