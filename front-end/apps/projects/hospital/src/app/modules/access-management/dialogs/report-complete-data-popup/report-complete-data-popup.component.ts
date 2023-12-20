import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, } from '@angular/material/dialog';
import { EReferenceRegulationState, ERole, ReferenceCompleteDataDto, ReferenceDataDto, ReferenceRegulationDto } from '@api-rest/api-model';
import { InstitutionalReferenceReportService } from '@api-rest/services/institutional-reference-report.service';
import { ContactDetails } from '@access-management/components/contact-details/contact-details.component';
import { PatientSummary } from '../../../hsi-components/patient-summary/patient-summary.component';
import { BehaviorSubject, Observable, map, of, switchMap, take, tap } from 'rxjs';
import { AppointmentSummary } from '@access-management/components/appointment-summary/appointment-summary.component';
import { APPOINTMENT_STATES_ID } from '@turnos/constants/appointment';
import { Tabs } from '@turnos/constants/tabs';
import { toPatientSummary, toContactDetails, toAppointmentSummary } from '@access-management/utils/mapper.utils';
import { PENDING } from '@access-management/constants/reference';
import { ContextService } from '@core/services/context.service';
import { NO_INSTITUTION } from '../../../home/home.component';
import { InstitutionalNetworkReferenceReportService } from '@api-rest/services/institutional-network-reference-report.service';
import { RegisterEditor } from '@presentation/components/register-editor-info/register-editor-info.component';
import { PermissionsService } from '@core/services/permissions.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { convertDateTimeDtoToDate } from '@api-rest/mapper/date-dto.mapper';


const GESTORES = [ERole.GESTOR_DE_ACCESO_DE_DOMINIO, ERole.GESTOR_DE_ACCESO_LOCAL, ERole.GESTOR_DE_ACCESO_REGIONAL];

@Component({
	selector: 'app-report-complete-data-popup',
	templateUrl: './report-complete-data-popup.component.html',
	styleUrls: ['./report-complete-data-popup.component.scss']
})
export class ReportCompleteDataPopupComponent implements OnInit {

	referenceCompleteData: ReferenceCompleteDataDto;
	reportCompleteData: ReportCompleteData;

	colapseContactDetails = false;

	Tabs = Tabs;
	referenceRegulationDto$: Observable<ReferenceRegulationDto>;
	approvedState = EReferenceRegulationState.APPROVED;
	observation: string;
	registerEditor: RegisterEditor = null;
	registerEditor$: BehaviorSubject<RegisterEditor> = new BehaviorSubject<RegisterEditor>(null);


	constructor(
		private readonly institutionalReferenceReportService: InstitutionalReferenceReportService,
		private readonly contextService: ContextService,
		private readonly institutionalNetworkReferenceReportService: InstitutionalNetworkReferenceReportService,
		private readonly permissionService: PermissionsService,
		private readonly snackBarService: SnackBarService,
		@Inject(MAT_DIALOG_DATA) public data,
	) { }

	ngOnInit(): void {
		const referenceDetails$ = this.getObservable();
		referenceDetails$.subscribe(
			referenceDetails => {
				this.setObservation(referenceDetails);
				this.referenceCompleteData = referenceDetails;
				this.referenceRegulationDto$ = of(this.referenceCompleteData.regulation);
				this.setReportData(this.referenceCompleteData);
				this.colapseContactDetails = this.referenceCompleteData.appointment?.appointmentStateId === APPOINTMENT_STATES_ID.SERVED;
			});
	}

	updateApprovalStatus() {
		const referenceDetails$ = this.getObservable();
		this.referenceRegulationDto$ = referenceDetails$.pipe(
			map(referenceDetails => { return referenceDetails.regulation }),
			tap(regulationNewState => this.referenceCompleteData = { ...this.referenceCompleteData, regulation: regulationNewState })
		);
	}

	private getObservable(): Observable<ReferenceCompleteDataDto> {
		return this.contextService.institutionId === NO_INSTITUTION ?
			this.institutionalNetworkReferenceReportService.getReferenceDetail(this.data.referenceId) :
			this.institutionalReferenceReportService.getReferenceDetail(this.data.referenceId);
	}

	addObservation(observation: string) {
		this.permissionService.hasContextAssignments$(GESTORES)
			.pipe(
				take(1),
				switchMap(hasRole => hasRole
					? this.addObservationGestores(observation)
					: this.addObservationOtherRoles(observation))
			)
			.subscribe(res => {
				if (res) {
					this.snackBarService.showSuccess('turnos.report-complete-data.SHOW_SUCCESS_OBSERVATION');
					this.updateObservations();
				}
				else
					this.snackBarService.showError('turnos.report-complete-data.SHOW_ERROR_OBSERVATION');
			});
	}

	private updateObservations() {
		const referenceDetails$ = this.getObservable();

		referenceDetails$.subscribe(
			referenceDetails => {
				this.setObservation(referenceDetails)
			})
	}

	private addObservationGestores(observation: string) {
		return this.institutionalNetworkReferenceReportService.addObservation(this.data.referenceId, observation);
	}

	private addObservationOtherRoles(observation: string) {
		return this.institutionalReferenceReportService.addObservation(this.data.referenceId, observation);
	}

	private setObservation(referenceDetails: ReferenceCompleteDataDto) {
		if (referenceDetails?.observation) {
			const { observation, createdBy, date } = referenceDetails.observation;
			this.observation = observation;
			this.registerEditor = { createdBy, date: convertDateTimeDtoToDate(date) };
			this.registerEditor$.next(this.registerEditor);
		}
	}

	private setReportData(referenceDetails: ReferenceCompleteDataDto): void {
		const patient = referenceDetails.patient;
		const pendingAppointment: AppointmentSummary = { state: PENDING };
		this.reportCompleteData = {
			patient: toPatientSummary(patient),
			contactDetails: toContactDetails(patient),
			reference: referenceDetails.reference,
			appointment: referenceDetails.appointment ? toAppointmentSummary(referenceDetails.appointment) : pendingAppointment,
		}
	}
}

export interface ReportCompleteData {
	patient: PatientSummary;
	contactDetails: ContactDetails;
	reference: ReferenceDataDto;
	appointment: AppointmentSummary;
}
