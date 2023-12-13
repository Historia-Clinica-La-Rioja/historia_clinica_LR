package net.pladema.clinichistory.requests.servicerequests.domain;

import ar.lamansys.sgh.clinichistory.domain.document.IDocumentBo;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ConclusionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DocumentObservationsBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class InformerObservationBo implements IDocumentBo {

	private Long id;
	private Integer encounterId;
	private String evolutionNote;
	private List<ConclusionBo> conclusions;
	private Integer patientId;
	private Integer institutionId;
	private String createdBy;
	private LocalDateTime actionTime;
	private PatientInfoBo patientInfo;
	private boolean confirmed;
	private LocalDateTime performedDate;

	@Override
	public short getDocumentType() { return DocumentType.MEDICAL_IMAGE_REPORT;	}

	@Override
	public Short getDocumentSource() {
		return SourceType.MEDICAL_IMAGE;
	}

	@Override
	public DocumentObservationsBo getNotes() {
		DocumentObservationsBo result = new DocumentObservationsBo();
		result.setEvolutionNote(evolutionNote);
		return result;
	}
}