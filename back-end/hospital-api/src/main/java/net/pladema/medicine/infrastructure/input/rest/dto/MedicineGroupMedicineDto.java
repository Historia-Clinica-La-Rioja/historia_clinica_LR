package net.pladema.medicine.infrastructure.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MedicineGroupMedicineDto implements Serializable {

	private static final long serialVersionUID = 4276123842871348L;

	private Integer id;
	private Integer medicineGroupId;
	private Integer medicineId;
	private Boolean financed;
	private String conceptPt;

}
