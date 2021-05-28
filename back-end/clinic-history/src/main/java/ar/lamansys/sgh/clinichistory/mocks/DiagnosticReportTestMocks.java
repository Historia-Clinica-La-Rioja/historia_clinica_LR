package ar.lamansys.sgh.clinichistory.mocks;


import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentDiagnosticReport;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.DiagnosticReport;

public class DiagnosticReportTestMocks {
    public static DiagnosticReport createDiagnosticReport(Integer patientId, Integer snomedId, String statusId, String cie10Codes,
                                                          Long noteId, Integer healthConditionId) {
        var result = new DiagnosticReport(patientId, snomedId, cie10Codes, noteId, healthConditionId);
        result.setStatusId(statusId);
        return result;
    }

    public static DocumentDiagnosticReport createDocumentDiagnosticReport(Long documentId, Integer diagnosticReportId){
        DocumentDiagnosticReport result = new DocumentDiagnosticReport(documentId, diagnosticReportId);
        return result;
    }
}
