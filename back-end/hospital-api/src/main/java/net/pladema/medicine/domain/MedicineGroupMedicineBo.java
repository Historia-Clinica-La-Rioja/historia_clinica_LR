package net.pladema.medicine.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MedicineGroupMedicineBo implements Serializable {

	private static final long serialVersionUID = 2446654484732250647L;

	private Integer id;
	private Integer medicineGroupId;
	private Integer medicineId;
	private Boolean financed;
	private String conceptPt;

}
