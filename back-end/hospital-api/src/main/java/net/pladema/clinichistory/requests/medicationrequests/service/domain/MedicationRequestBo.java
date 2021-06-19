package net.pladema.clinichistory.requests.medicationrequests.service.domain;


import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import lombok.Getter;
import lombok.Setter;
import ar.lamansys.sgh.clinichistory.domain.document.IDocumentBo;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.MedicationBo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    public short getDocumentType() {
        return DocumentType.RECIPE;
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
