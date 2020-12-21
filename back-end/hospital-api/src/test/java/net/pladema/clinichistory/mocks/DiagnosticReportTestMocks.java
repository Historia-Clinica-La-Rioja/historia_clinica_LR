package net.pladema.clinichistory.mocks;

import net.pladema.clinichistory.documents.repository.entity.DocumentDiagnosticReport;
import net.pladema.clinichistory.documents.repository.ips.entity.DiagnosticReport;

public class DiagnosticReportTestMocks {
    public static DiagnosticReport createDiagnosticReport(Integer patientId, Integer snomedId, String cie10Codes,
                                                          Long noteId, Integer healthConditionId) {
        return new DiagnosticReport(patientId, snomedId, cie10Codes, noteId, healthConditionId);
    }

    public static DocumentDiagnosticReport createDocumentDiagnosticReport(Long documentId, Integer diagnosticReportId){
        DocumentDiagnosticReport result = new DocumentDiagnosticReport(documentId, diagnosticReportId);
        return result;
    }
}
