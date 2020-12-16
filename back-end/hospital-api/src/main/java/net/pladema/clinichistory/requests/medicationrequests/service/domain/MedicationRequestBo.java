package net.pladema.clinichistory.requests.medicationrequests.service.domain;


import lombok.Getter;
import lombok.Setter;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentType;
import net.pladema.clinichistory.documents.service.Document;
import net.pladema.clinichistory.documents.service.ips.domain.MedicationBo;
import net.pladema.clinichistory.outpatient.repository.domain.SourceType;
import net.pladema.patient.controller.dto.BasicPatientDto;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MedicationRequestBo implements Document {

    private Integer patientId;

    private BasicPatientDto patientDto;

    private Integer medicalCoverageId;

    private Integer doctorId;

    private Integer encounterId;

    private List<MedicationBo> medications = new ArrayList<>();

    private boolean hasRecipe = true;

    public short getDocumentType() {
        return DocumentType.RECIPE;
    }

    @Override
    public Short getDocumentSource() {
        return SourceType.RECIPE;
    }

}
