import { ReportCompleteDataPopupComponent } from '@access-management/dialogs/report-complete-data-popup/report-complete-data-popup.component';
import { DashboardService } from '@access-management/services/dashboard.service';
import { SearchAppointmentsInfoService } from '@access-management/services/search-appointment-info.service';
import { Component, Input } from '@angular/core';
import { MatDialogRef, MatDialog } from '@angular/material/dialog';
import { ERole, ReferenceCompleteDataDto } from '@api-rest/api-model';
import { PermissionsService } from '@core/services/permissions.service';
import { APPOINTMENT_STATES_ID } from '@turnos/constants/appointment';
import { Tabs } from '@turnos/constants/tabs';
import { CancelAppointmentComponent } from '@turnos/dialogs/cancel-appointment/cancel-appointment.component';
import { TabsService } from '@turnos/services/tabs.service';
import { take } from 'rxjs';
import { DashboardView } from '../reference-dashboard-filters/reference-dashboard-filters.component';

@Component({
	selector: 'app-institutional-actions',
	templateUrl: './institutional-actions.component.html',
	styleUrls: ['./institutional-actions.component.scss']
})
export class InstitutionalActionsComponent {

	@Input() set reportCompleteData(reportData: ReferenceCompleteDataDto) {
		if (reportData) {
			this.refenceCompleteDto = reportData;
			this.setActions();
		}
	};

	refenceCompleteDto: ReferenceCompleteDataDto;
	actions: InstitutionalActions;

	constructor(
		private readonly searchAppointmentsInfoService: SearchAppointmentsInfoService,
		private readonly permissionService: PermissionsService,
		private dialogRef: MatDialogRef<ReportCompleteDataPopupComponent>,
		private readonly dashboardService: DashboardService,
		private readonly tabsService: TabsService,
		private readonly dialog: MatDialog,
	) { }

	assignAppointmentInInstitution(): void {
		this.searchAppointmentsInfoService.loadInformation(this.refenceCompleteDto.patient.patientId, this.refenceCompleteDto.reference);
		this.dialogRef.close();
		this.tabsService.setTab(Tabs.INSTITUTION);
	}

	assignAppointmentInCareNetwork(): void {
		this.searchAppointmentsInfoService.loadInformation(this.refenceCompleteDto.patient.patientId, this.refenceCompleteDto.reference);
		this.dialogRef.close();
		this.tabsService.setTab(Tabs.CARE_NETWORK);
	}

	cancelAppointment(): void {
		const dialogRef = this.dialog.open(CancelAppointmentComponent, {
			data: {
				appointmentId: this.refenceCompleteDto.appointment.appointmentId
			}
		});
		dialogRef.afterClosed().subscribe(canceledAppointment => {
			if (canceledAppointment) {
				this.dashboardService.updateReports();
				this.dialogRef.close();
			}
		});
	}

	private setActions(): void {
		const appointment = this.refenceCompleteDto.appointment;
		const canCancelAppointment = appointment?.appointmentStateId === APPOINTMENT_STATES_ID.ASSIGNED || appointment?.appointmentStateId === APPOINTMENT_STATES_ID.CONFIRMED;
		this.setCancelAppointment(canCancelAppointment);
		if (!this.refenceCompleteDto.reference.closureType) {
			const assignAppointment = !appointment || appointment?.appointmentStateId === APPOINTMENT_STATES_ID.ABSENT;

			if (assignAppointment)
				this.permissionService.hasContextAssignments$([ERole.ADMINISTRATIVO])
					.pipe(take(1))
					.subscribe(hasRole => hasRole ? this.setAdministrativeActions() : this.setMedicalActions())
		}
	}

	private setAdministrativeActions(): void {
		const dashboardView = this.dashboardService.dashboardView;
		const careLineId = this.refenceCompleteDto.reference.careLine.id;

		if (dashboardView == DashboardView.RECEIVED) {
			this.setAssignAppointmentInInstitution(true);
			this.searchAppointmentsInfoService.searchAppointmentsInTabs = !!careLineId;
		} else if (careLineId) {
			this.setAssignAppointmentInCareNetwork(true);
			this.searchAppointmentsInfoService.searchAppointmentsInTabs = false;
		}

	}

	private setMedicalActions(): void {
		if (this.refenceCompleteDto.reference.careLine.id)
			this.setAssignAppointmentInCareNetwork(true);
	}

	private setCancelAppointment(value: boolean): void {
		this.actions = {
			...this.actions,
			cancelAppointment: value
		}
	}

	private setAssignAppointmentInInstitution(value: boolean): void {
		this.actions = {
			...this.actions,
			assignAppointmentInInstitution: value
		}
	}

	private setAssignAppointmentInCareNetwork(value: boolean): void {
		this.actions = {
			...this.actions,
			assignAppointmentInCareNetwork: value
		}
	}

}

export interface InstitutionalActions {
	assignAppointmentInCareNetwork: boolean;
	assignAppointmentInInstitution: boolean;
	cancelAppointment: boolean;
}
