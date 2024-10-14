package net.pladema.medicine.infrastructure.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MedicineGroupProblemDto implements Serializable {

	private static final long serialVersionUID = 2446654484732250647L;

	private Integer id;

	private Integer medicineGroupId;

	private Integer problemId;

	private String conceptPt;

}
