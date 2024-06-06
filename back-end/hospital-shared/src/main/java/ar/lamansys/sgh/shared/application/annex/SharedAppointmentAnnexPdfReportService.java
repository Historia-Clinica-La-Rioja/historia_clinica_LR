package ar.lamansys.sgh.shared.application.annex;

import ar.lamansys.sgh.shared.domain.annex.SharedAppointmentAnnexPdfReportResponse;

public interface SharedAppointmentAnnexPdfReportService {
	SharedAppointmentAnnexPdfReportResponse run(Integer appointmentId);
}
