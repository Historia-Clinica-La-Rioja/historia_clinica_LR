import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { ReferenceAppointmentDto, ReferenceCompleteDataDto, ReferenceDataDto, ERole, EReferenceClosureType } from '@api-rest/api-model';
import { ReferenceReportService } from '@api-rest/services/reference-report.service';
import { ContactDetails } from '@turnos/components/contact-details/contact-details.component';
import { PatientSummary } from '../../../hsi-components/patient-summary/patient-summary.component';
import { Observable, take, tap } from 'rxjs';
import { AppointmentSummary } from '@turnos/components/appointment-summary/appointment-summary.component';
import { PENDING } from '@turnos/utils/reference.utils';
import { APPOINTMENT_STATES_ID } from '@turnos/constants/appointment';
import { Tabs } from '@turnos/constants/tabs';
import { PermissionsService } from '@core/services/permissions.service';
import { toPatientSummary, toContactDetails, toAppointmentSummary } from '@turnos/utils/mapper.utils';
import { ReferenceView } from '@turnos/components/reference-report/reference-report.component';
import { SearchAppointmentsInfoService } from '@turnos/services/search-appointment-info.service';
import { TabsService } from '@turnos/services/tabs.service';
import { ReferenceReportFacadeService } from '@turnos/services/reference-report-facade.service';
import { CancelAppointmentComponent } from '../cancel-appointment/cancel-appointment.component';

@Component({
	selector: 'app-report-complete-data-popup',
	templateUrl: './report-complete-data-popup.component.html',
	styleUrls: ['./report-complete-data-popup.component.scss']
})
export class ReportCompleteDataPopupComponent implements OnInit {

	referenceCompleteData$: Observable<ReferenceCompleteDataDto>;
	reportCompleteData: ReportCompleteData;

	colapseContactDetails = false;
	popupActions: PopUpActions;

	Tabs = Tabs;
	private appointmentId: number;

	constructor(
		private readonly referenceReportService: ReferenceReportService,
		private readonly searchAppointmentsInfoService: SearchAppointmentsInfoService,
		private readonly permissionService: PermissionsService,
		public dialogRef: MatDialogRef<ReportCompleteDataPopupComponent>,
		private readonly referenceReportFacade: ReferenceReportFacadeService,
		private readonly tabsService: TabsService,
		private readonly dialog: MatDialog,
		@Inject(MAT_DIALOG_DATA) public data,
	) { }

	ngOnInit(): void {
		this.referenceCompleteData$ = this.referenceReportService.getReferenceDetail(this.data.referenceId).pipe(tap(
			referenceDetails => {
				this.setReportData(referenceDetails);
				this.colapseContactDetails = referenceDetails.appointment?.appointmentStateId === APPOINTMENT_STATES_ID.SERVED;
				this.setPopUpActions(referenceDetails.appointment, referenceDetails.reference.closureType);
			}));
	}

	assignAppointmentInInstitution(): void {
		this.searchAppointmentsInfoService.loadInformation(this.reportCompleteData.patient.id, this.reportCompleteData.reference);
		this.dialogRef.close();
		this.tabsService.setTab(Tabs.INSTITUTION);
	}

	assignAppointmentInCareNetwork(): void {
		this.searchAppointmentsInfoService.loadInformation(this.reportCompleteData.patient.id, this.reportCompleteData.reference);
		this.dialogRef.close();
		this.tabsService.setTab(Tabs.CARE_NETWORK);
	}

	cancelAppointment(): void {
		const dialogRef = this.dialog.open(CancelAppointmentComponent, {
			data: {
				appointmentId: this.appointmentId
			}
		});
		dialogRef.afterClosed().subscribe(canceledAppointment => {
			if (canceledAppointment) {
				this.referenceReportFacade.updateReports();
				this.dialogRef.close();
			}
		});
	}


	private setReportData(referenceDetails: ReferenceCompleteDataDto): void {
		const patient = referenceDetails.patient;
		const pendingAppointment: AppointmentSummary = { state: PENDING };
		this.appointmentId = referenceDetails.appointment?.appointmentId;
		this.reportCompleteData = {
			patient: toPatientSummary(patient),
			contactDetails: toContactDetails(patient),
			reference: referenceDetails.reference,
			appointment: referenceDetails.appointment ? toAppointmentSummary(referenceDetails.appointment) : pendingAppointment
		}
	}

	private setPopUpActions(appointment: ReferenceAppointmentDto, closureType: EReferenceClosureType): void {
		this.setCancelAppointment(appointment?.appointmentStateId === APPOINTMENT_STATES_ID.ASSIGNED || appointment?.appointmentStateId === APPOINTMENT_STATES_ID.CONFIRMED);
		if (!closureType) {
			const assignAppointment = !appointment || appointment?.appointmentStateId === APPOINTMENT_STATES_ID.ABSENT;

			if (assignAppointment)
				this.permissionService.hasContextAssignments$([ERole.ADMINISTRATIVO])
					.pipe(take(1))
					.subscribe(hasRole => hasRole ? this.setAdministrativeActions() : this.setMedicalActions())
		}
	}

	private setAdministrativeActions(): void {
		const dashboardView = this.referenceReportFacade.dashboardView;
		const careLineId = this.reportCompleteData.reference.careLine.id;

		if (dashboardView == ReferenceView.RECEIVED) {
			this.setAssignAppointmentInInstitution(true);
			this.searchAppointmentsInfoService.searchAppointmentsInTabs = !!careLineId;
		} else if (careLineId) {
			this.setAssignAppointmentInCareNetwork(true);
			this.searchAppointmentsInfoService.searchAppointmentsInTabs = false;
		}

	}

	private setMedicalActions(): void {
		if (this.reportCompleteData.reference.careLine.id)
			this.setAssignAppointmentInCareNetwork(true);
	}

	private setCancelAppointment(value: boolean): void {
		this.popupActions = {
			...this.popupActions,
			cancelAppointment: value
		}
	}

	private setAssignAppointmentInInstitution(value: boolean): void {
		this.popupActions = {
			...this.popupActions,
			assignAppointmentInInstitution: value
		}
	}

	private setAssignAppointmentInCareNetwork(value: boolean): void {
		this.popupActions = {
			...this.popupActions,
			assignAppointmentInCareNetwork: value
		}
	}

}

interface ReportCompleteData {
	patient: PatientSummary;
	contactDetails: ContactDetails;
	reference: ReferenceDataDto;
	appointment: AppointmentSummary;
}

interface PopUpActions {
	assignAppointmentInCareNetwork: boolean;
	assignAppointmentInInstitution: boolean;
	cancelAppointment: boolean;
}
