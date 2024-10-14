package net.pladema.clinichistory.indication.service.diet.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import ar.lamansys.sgh.clinichistory.domain.document.IDocumentBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class GenericIndicationBo implements IDocumentBo {

	protected Long id;

	protected Integer patientId;

	protected Short statusId;

	protected LocalDate indicationDate;

	protected Integer institutionId;

	protected Short typeId;

	protected Integer encounterId;

	protected LocalDateTime createdOn;

	protected Integer professionalId;

	protected Short sourceTypeId;

	private Map<String, Object> contextMap;

	@Override
	public short getDocumentType() {
		return DocumentType.INDICATION;
	}

	@Override
	public Short getDocumentSource() {
		return sourceTypeId;
	}
}
