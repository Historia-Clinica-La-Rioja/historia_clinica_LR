package net.pladema.clinichistory.requests.medicationrequests.service.domain;


import lombok.Getter;
import lombok.Setter;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentType;
import net.pladema.clinichistory.documents.service.IDocumentBo;
import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.documents.service.ips.domain.MedicationBo;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.SourceType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MedicationRequestBo implements IDocumentBo {

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
