package ar.lamansys.sgh.publicapi.prescription.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Builder
public class LineStatusBo {
	private Integer medicationStatementId;
	private Integer prescriptionLineNumber;
	private Short prescriptionLineState;
	private String pharmacyName;
}
