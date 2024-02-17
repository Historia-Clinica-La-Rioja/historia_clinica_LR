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
public class DigitalSignatureCallbackBo {

	private DigitalSignatureStatusBo status;

	private Long documentId;

	private Integer personId;

	private String hash;

	private String signatureHash;

}
