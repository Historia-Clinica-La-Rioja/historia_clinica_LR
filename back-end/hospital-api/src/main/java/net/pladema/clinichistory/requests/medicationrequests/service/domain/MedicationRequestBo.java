package net.pladema.clinichistory.requests.medicationrequests.service.domain;


import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import lombok.Getter;
import lombok.Setter;
import ar.lamansys.sgh.clinichistory.domain.document.IDocumentBo;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.MedicationBo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class MedicationRequestBo implements IDocumentBo {

    private Long id;

    private Integer medicationRequestId;

    private PatientInfoBo patientInfo;

    private Integer medicalCoverageId;

    private Integer doctorId;

    private Integer encounterId;

    private Integer institutionId;

    private List<MedicationBo> medications = new ArrayList<>();

    private boolean hasRecipe = false;

    private LocalDate requestDate = LocalDate.now();

	private Boolean isPostDated;

	private Integer repetitions;

	private Integer clinicalSpecialtyId;

	private Boolean isArchived;

	private UUID uuid;

    private Map<String, Object> contextMap;

	public short getDocumentType() {
        return DocumentType.RECIPE;
    }

	@Override
	public LocalDateTime getPerformedDate() {
		return requestDate.atStartOfDay();
	}

    @Override
    public Short getDocumentSource() {
        return SourceType.RECIPE;
    }

    @Override
    public Integer getPatientId() {
        if (patientInfo == null)
            return null;
        return patientInfo.getId();
    }

}
