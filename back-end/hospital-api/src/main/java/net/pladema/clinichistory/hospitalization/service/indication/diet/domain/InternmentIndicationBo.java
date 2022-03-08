package net.pladema.clinichistory.hospitalization.service.indication.diet.domain;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import ar.lamansys.sgh.clinichistory.domain.document.IDocumentBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class InternmentIndicationBo implements IDocumentBo {

	protected Long id;

	protected Integer patientId;

	protected Short statusId;

	@JsonFormat(pattern = JacksonDateFormatConfig.DATE_TIME_FORMAT)
	protected LocalDateTime indicationDate;

	protected Integer institutionId;

	protected Short typeId;

	protected Integer encounterId;

	@Override
	public short getDocumentType() {
		return DocumentType.INDICATION;
	}

	@Override
	public Short getDocumentSource() {
		return SourceType.HOSPITALIZATION;
	}
}
