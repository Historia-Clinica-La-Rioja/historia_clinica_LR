package net.pladema.clinichistory.hospitalization.service.indication.diet.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import ar.lamansys.sgh.clinichistory.domain.document.IDocumentBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class InternmentIndicationBo implements IDocumentBo {

	protected Long id;

	protected Integer patientId;

	protected Short statusId;

	protected LocalDate indicationDate;

	protected Integer institutionId;

	protected Short typeId;

	protected Integer encounterId;

	protected LocalDateTime createdOn;

	protected Integer professionalId;

	@Override
	public short getDocumentType() {
		return DocumentType.INDICATION;
	}

	@Override
	public Short getDocumentSource() {
		return SourceType.HOSPITALIZATION;
	}
}
