package net.pladema.reports.service;

import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.reports.domain.AppointmentConsultationSummaryBo;
import net.pladema.reports.domain.ReportSearchFilterBo;
import net.pladema.reports.repository.InstitutionInfo;

import java.util.List;

public interface AppointmentConsultationSummaryExcelService {

	IWorkbook buildAppointmentConsultationSummaryExcelReport(String title,
															 ReportSearchFilterBo filter,
															 InstitutionInfo institutionInfo,
															 List<AppointmentConsultationSummaryBo> appointmentConsultationSummaryData,
															 AppointmentState appointmentState);

}
