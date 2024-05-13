package net.pladema.reports.application.ports;

import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.reports.domain.AppointmentConsultationSummaryBo;
import net.pladema.reports.domain.ReportSearchFilterBo;

import java.util.List;

public interface AppointmentConsultationSummaryStorage {

	List<AppointmentConsultationSummaryBo> fetchAppointmentConsultationSummary(ReportSearchFilterBo filter);
	AppointmentState getAppointmentStateById(Short id);
}
