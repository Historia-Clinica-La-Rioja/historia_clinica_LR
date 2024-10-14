package ar.lamansys.sgh.clinichistory.application.anestheticreport.ports.output;

import ar.lamansys.sgh.clinichistory.domain.document.IEditableDocumentBo;

public interface AnestheticReportOutputPort {

    Boolean validateAnestheticReport(IEditableDocumentBo documentBo);
}
