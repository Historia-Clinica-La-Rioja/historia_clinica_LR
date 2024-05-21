package net.pladema.medicine.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MedicineFinancingStatusBo implements Serializable {

	private Integer id;
	private String conceptSctid;
	private String conceptPt;
	private Boolean financed;

}
