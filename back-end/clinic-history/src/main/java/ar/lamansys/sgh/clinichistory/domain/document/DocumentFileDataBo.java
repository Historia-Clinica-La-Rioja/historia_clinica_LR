package ar.lamansys.sgh.clinichistory.domain.document;

import ar.lamansys.sgh.shared.infrastructure.output.entities.ESignatureStatus;
import lombok.Getter;

@Getter
public class DocumentFileDataBo {

    private Long id;

    private String filename;

	private SignatureStatusTypeBo signatureStatusType;

    public DocumentFileDataBo(Long id, String filename, Short signatureStatusId) {
        this.id = id;
        this.filename = filename;
		this.signatureStatusType = new SignatureStatusTypeBo(signatureStatusId, ESignatureStatus.map(signatureStatusId).getValue());
    }
}
