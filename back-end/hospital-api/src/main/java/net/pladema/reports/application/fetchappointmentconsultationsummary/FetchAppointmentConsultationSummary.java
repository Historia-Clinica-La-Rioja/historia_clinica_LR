package net.pladema.reports.application.fetchappointmentconsultationsummary;

import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.reports.application.fetchappointmentconsultationsummary.exception.AppointmentCSReportException;
import net.pladema.reports.application.fetchappointmentconsultationsummary.exception.EAppointmentCSReportException;

import net.pladema.reports.application.ports.AppointmentConsultationSummaryStorage;
import net.pladema.reports.application.ports.InstitutionReportStorage;
import net.pladema.reports.domain.AppointmentConsultationSummaryBo;
import net.pladema.reports.domain.ReportSearchFilterBo;

import net.pladema.reports.repository.InstitutionInfo;

import net.pladema.reports.service.AppointmentConsultationSummaryExcelService;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class FetchAppointmentConsultationSummary {

	private static final Integer ONE_WEEK = 7;
	private static final LocalDate MIN_DATE = LocalDate.of(1900, 1, 1);
	private final AppointmentConsultationSummaryStorage appointmentConsultationSummaryStorage;
	private final InstitutionReportStorage institutionReportStorage;
	private final AppointmentConsultationSummaryExcelService appointmentConsultationSummaryExcelService;

	public IWorkbook run(String title, ReportSearchFilterBo filter) {
		log.debug("Fetch Appointment Consultation Summary Report by title {} and filter {} ", title, filter);
		validateDates(filter.getFromDate(), filter.getToDate());
		return getAppointmentConsultationSummaryExcelReport(title, filter);
	}
	private void validateDates(LocalDate from, LocalDate to) {
		if (ChronoUnit.DAYS.between(from, to) > ONE_WEEK)
			throw new AppointmentCSReportException(EAppointmentCSReportException.INVALID_DATES, "La fecha ingresada debe contemplar como máximo un lapso de una semana");
		if (from.isBefore(MIN_DATE))
			throw new AppointmentCSReportException(EAppointmentCSReportException.INVALID_DATES, "La fecha ingresada no puede ser anterior a 1900");
	}

	private IWorkbook getAppointmentConsultationSummaryExcelReport(String title, ReportSearchFilterBo filter){
		InstitutionInfo institutionInfo = institutionReportStorage.getInstitutionInfo(filter.getInstitutionId());
		List<AppointmentConsultationSummaryBo> appointmentConsultationSummaryData = appointmentConsultationSummaryStorage.fetchAppointmentConsultationSummary(filter);
		AppointmentState appointmentState = filter.getAppointmentStateId() == null ? null : appointmentConsultationSummaryStorage.getAppointmentStateById(filter.getAppointmentStateId());
		return appointmentConsultationSummaryExcelService.buildAppointmentConsultationSummaryExcelReport(
				title,
				filter,
				institutionInfo,
				appointmentConsultationSummaryData,
				appointmentState
		);
	}

}
