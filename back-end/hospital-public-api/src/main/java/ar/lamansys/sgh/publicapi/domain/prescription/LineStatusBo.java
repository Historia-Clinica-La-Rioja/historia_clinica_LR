package ar.lamansys.sgh.publicapi.domain.prescription;

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
	Integer medicationStatementId;
	Integer prescriptionLineNumber;
	Short prescriptionLineState;
}
