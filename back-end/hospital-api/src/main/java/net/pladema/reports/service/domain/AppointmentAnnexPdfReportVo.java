package net.pladema.reports.service.domain;

import ar.lamansys.sgh.shared.domain.annex.SharedAppointmentAnnexPdfReportResponse;
import ar.lamansys.sgx.shared.filestorage.application.FileContentBo;
import lombok.Value;

@Value
public class AppointmentAnnexPdfReportVo implements SharedAppointmentAnnexPdfReportResponse {
	private FileContentBo pdf;
	private String filename;

}
