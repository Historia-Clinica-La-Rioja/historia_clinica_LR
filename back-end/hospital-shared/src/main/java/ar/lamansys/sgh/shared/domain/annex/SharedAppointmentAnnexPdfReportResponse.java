package ar.lamansys.sgh.shared.domain.annex;

import ar.lamansys.sgx.shared.filestorage.application.FileContentBo;

public interface SharedAppointmentAnnexPdfReportResponse {
	FileContentBo getPdf();

	String getFilename();
}
