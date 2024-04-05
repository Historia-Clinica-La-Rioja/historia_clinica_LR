package net.pladema.electronicjointsignature.documentlist.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
@Getter
@Setter
public class ElectronicSignatureDocumentListFilterBo {

	private Integer institutionId;

	private Integer healthcareProfessionalId;

	private Short signatureStatusId;

	public ElectronicSignatureDocumentListFilterBo(Integer institutionId, Short signatureStatusId) {
		this.institutionId = institutionId;
		this.signatureStatusId = signatureStatusId;
	}

}
