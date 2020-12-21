package net.pladema.clinichistory.requests.servicerequests.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentType;
import net.pladema.clinichistory.documents.service.Document;
import net.pladema.clinichistory.outpatient.repository.domain.SourceType;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRequestBo implements Document {

    private Integer patientId;
    private String categoryId;
    private Integer medicalCoverageId;
    private Integer encounterId;
    private Integer doctorId;
    private Long noteId;
    private List<DiagnosticReportBo> diagnosticReports;

    @Override
    public short getDocumentType() {
        return DocumentType.ORDER;
    }

    @Override
    public Short getDocumentSource() {
        return SourceType.ORDER;
    }
}
