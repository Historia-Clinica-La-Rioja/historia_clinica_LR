package ar.lamansys.sgh.clinichistory.domain.document.digitalsignature;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DigitalSignatureStatusBo {

	private Boolean success;

	private String msg;
}
