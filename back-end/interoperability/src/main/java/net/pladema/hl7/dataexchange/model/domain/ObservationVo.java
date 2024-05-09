package net.pladema.hl7.dataexchange.model.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ObservationVo {

	private String id;
	private String status;
	private String categoryCode;
	private String code;
	private String patientId;
	private String doctorId;

	private String valueString;
	private Integer valueInteger;
	private Boolean valueBoolean;
	private Float quantityValue;
	private String quantityUnit;

	private String bodyPartCode;
	private String note;

	public boolean hasQuantity() {
		return quantityValue != null && quantityUnit != null;
	}

}
