import { Component, OnInit } from '@angular/core';
import { REPORT_TYPES_ID } from '../../constants/report-types';
import { ReportsService } from '@api-rest/services/reports.service';
import { AppFeature, ImageNetworkProductivityFilterDto } from '@api-rest/api-model';
import { ReportFilters } from '../home/home.component';
import { dateToDateDto } from '@api-rest/mapper/date-dto.mapper';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { InstitutionReportType } from '@api-rest/services/report-institution-queue.service';
import { MonthlyQueueService } from '../../components/monthly-queue/monthly-queue.service';

@Component({
	selector: 'app-monthly-report',
	templateUrl: './monthly-report.component.html',
	styleUrls: ['./monthly-report.component.scss']
})
export class MonthlyReportComponent implements OnInit {
	isLoadingRequestReport = false;
	private useQueue = false;
	constructor(
		private reportsService: ReportsService,
		private featureFlagService: FeatureFlagService,
		private monthlyQueueService: MonthlyQueueService,
	) { }

	ngOnInit(): void {
		this.featureFlagService.isActive(AppFeature.HABILITAR_REPORTES_INSTANTANEOS).subscribe(
			isOn => this.useQueue = !isOn
		)
	}

	generate({reportId, reportDescription, reportFilters}) {

		const selectedReport = this.queueReport({reportId, reportDescription, reportFilters}) || this.selectedReport({reportId, reportDescription, reportFilters});
		if (selectedReport) {
			this.isLoadingRequestReport = true;
			selectedReport.subscribe(() => this.isLoadingRequestReport = false);
		}
	}

	private queueReport({reportId, reportDescription, reportFilters}) {
		if (!this.useQueue) {
			return null;
		}
		const fileName = `${reportDescription}.xls`;
		if (reportId === REPORT_TYPES_ID.MONTHLY) {
			return this.monthlyQueueService.showDialog(
				InstitutionReportType.Monthly,
				reportFilters,
				fileName,
			);
		}
		if (reportId === REPORT_TYPES_ID.NOMINAL_APPOINTMENTS_DETAIL) {
			return this.monthlyQueueService.showDialog(
				InstitutionReportType.AppointmentNominalDetail,
				reportFilters,
				fileName,
			)
		}



	}

	private selectedReport({reportId, reportDescription, reportFilters}) {
		const getReportById = {
			[REPORT_TYPES_ID.MONTHLY]: this.reportsService.getMonthlyReport(reportFilters, `${reportDescription}.xls`),
			[REPORT_TYPES_ID.OUTPATIENT_SUMMARY_REPORT]: this.reportsService.getOutpatientSummaryReport(reportFilters, `${reportDescription}.xls`),
			[REPORT_TYPES_ID.MONTHLY_SUMMARY_OF_EXTERNAL_CLINIC_APPOINTMENTS]:
			this.reportsService.getMonthlySummaryOfExternalClinicAppointmentsReport(reportFilters, `${reportDescription}.xls`),
			[REPORT_TYPES_ID.NOMINAL_APPOINTMENTS_DETAIL]:
			this.reportsService.getNominalAppointmentsDetail(reportFilters, `${reportDescription}.xls`),
			[REPORT_TYPES_ID.NOMINAL_DIAGNOSTIC_IMAGING]:
			this.reportsService.getImageNetworkProductivityReport(this.prepareImageNetworkProductivityFilterDto(reportFilters), `${reportDescription}.xls`),
			[REPORT_TYPES_ID.GUARD_ATTENTION_DETAIL_REPORT]: this.reportsService.getNominalEmergencyCareEpisodeDetail(reportFilters, `${reportDescription}.xls`)
		};

		return getReportById[reportId];

	}

	private prepareImageNetworkProductivityFilterDto(reportsFilters: ReportFilters): ImageNetworkProductivityFilterDto {
		return {
			clinicalSpecialtyId: reportsFilters.clinicalSpecialtyId,
			from: dateToDateDto(reportsFilters.fromDate),
			healthcareProfessionalId: reportsFilters.doctorId,
			to: dateToDateDto(reportsFilters.toDate),
		}
	}

}
