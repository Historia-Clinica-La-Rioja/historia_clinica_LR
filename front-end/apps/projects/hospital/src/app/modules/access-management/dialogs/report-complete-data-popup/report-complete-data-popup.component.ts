import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef, } from '@angular/material/dialog';
import { EReferenceRegulationState, ReferenceAppointmentDto, ReferenceCompleteDataDto, ReferenceDataDto, ReferenceRegulationDto, ReferenceAdministrativeStateDto } from '@api-rest/api-model';
import { InstitutionalReferenceReportService } from '@api-rest/services/institutional-reference-report.service';
import { ContactDetails } from '@access-management/components/contact-details/contact-details.component';
import { PatientSummary } from '../../../hsi-components/patient-summary/patient-summary.component';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { AppointmentSummary } from '@access-management/components/appointment-summary/appointment-summary.component';
import { APPOINTMENT_STATES_ID } from '@turnos/constants/appointment';
import { toPatientSummary, toContactDetails, toAppointmentSummary } from '@access-management/utils/mapper.utils';
import { PENDING } from '@access-management/constants/reference';
import { ContextService } from '@core/services/context.service';
import { NO_INSTITUTION } from '../../../home/home.component';
import { InstitutionalNetworkReferenceReportService } from '@api-rest/services/institutional-network-reference-report.service';
import { RegisterEditor } from '@presentation/components/register-editor-info/register-editor-info.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { convertDateTimeDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { DerivationEmmiter, RegisterDerivationEditor } from '../../components/derive-request/derive-request.component'
import { ReferencePermissionCombinationService } from '@access-management/services/reference-permission-combination.service';

@Component({
	selector: 'app-report-complete-data-popup',
	templateUrl: './report-complete-data-popup.component.html',
	styleUrls: ['./report-complete-data-popup.component.scss']
})
export class ReportCompleteDataPopupComponent implements OnInit {

	referenceCompleteData: ReferenceCompleteDataDto;
	reportCompleteData: ReportCompleteData;

	referenceRegulationDto$: Observable<ReferenceRegulationDto>;
	referenceAdministrativeStateDto$: Observable <ReferenceAdministrativeStateDto>;
	registerEditor$: BehaviorSubject<RegisterEditor> = new BehaviorSubject<RegisterEditor>(null);
	registerDeriveEditor$: BehaviorSubject<RegisterDerivationEditor> = new BehaviorSubject<RegisterDerivationEditor>(null);

	referenceAppointment: ReferenceAppointmentDto;
	auditedState = EReferenceRegulationState.AUDITED;
	waitingAuditedState = EReferenceRegulationState.WAITING_AUDIT;
	registerEditor: RegisterEditor = null;
	registerDeriveEditor: RegisterDerivationEditor = null;
	selectedFiles: File[] = [];
	
	observation: string;
	derivation: string;
	
	domainRole = false;
	colapseContactDetails = false;

	constructor(
		private readonly institutionalReferenceReportService: InstitutionalReferenceReportService,
		private readonly contextService: ContextService,
		private readonly institutionalNetworkReferenceReportService: InstitutionalNetworkReferenceReportService,
		private readonly snackBarService: SnackBarService,
		private dialogRef: MatDialogRef<ReportCompleteDataPopupComponent>,
		public permissionService: ReferencePermissionCombinationService,
		@Inject(MAT_DIALOG_DATA) public data,
	) { }

	ngOnInit(): void {
		this.domainRole = this.contextService.institutionId === NO_INSTITUTION;
		this.updateReference();
	}

	closeDialog() {
		this.dialogRef.close();
	}

	setReferenceDetails(referenceDetails$: Observable<ReferenceCompleteDataDto>) {
		referenceDetails$.subscribe(
			referenceDetails => {
				this.setAppointment(referenceDetails.appointment);
				this.setObservation(referenceDetails);
				this.setDerivation(referenceDetails);
				this.referenceCompleteData = referenceDetails;
				this.referenceRegulationDto$ = of(this.referenceCompleteData.regulation);
				this.referenceAdministrativeStateDto$ = of(this.referenceCompleteData.administrativeState);
				this.setReportData(this.referenceCompleteData);
				this.permissionService.setReferenceAndReportDataAndVisualPermissions(this.referenceCompleteData, this.reportCompleteData);
				this.colapseContactDetails = this.referenceCompleteData.appointment?.appointmentStateId === APPOINTMENT_STATES_ID.SERVED;
			});
	}

	private getObservable(): Observable<ReferenceCompleteDataDto> {
		return this.domainRole ?
			this.institutionalNetworkReferenceReportService.getReferenceDetail(this.data.referenceId) :
			this.institutionalReferenceReportService.getReferenceDetail(this.data.referenceId);
	}

	updateReference() {
		const referenceDetails$ = this.getObservable();
		this.setReferenceDetails(referenceDetails$);
	}

	addObservation(observation: string) {
		let observationObservable;
		if(this.permissionService.isRoleGestor)
			observationObservable = this.addObservationGestores(observation);
		else 
			observationObservable = this.addObservationOtherRoles(observation);

		observationObservable.subscribe(response => {
			if (response) {
				this.snackBarService.showSuccess('turnos.report-complete-data.SHOW_SUCCESS_OBSERVATION');
				this.updateObservations();
			}
			else
				this.snackBarService.showError('turnos.report-complete-data.SHOW_ERROR_OBSERVATION');
		});
	}

	performDerivationAction (derivation: DerivationEmmiter): void {
		this.derivation = derivation.derivation;
		if (derivation.canEdit)
			this.editDerivation(derivation.derivation);
		else
			this.addDerivation(derivation.derivation);
	}

	editDerivation(derivation: string): void {
		this.institutionalNetworkReferenceReportService.updateDerivation(this.registerDeriveEditor$.getValue().id, derivation)
		.subscribe(editSuccess => {
			if (editSuccess) {
				this.snackBarService.showSuccess('access-management.derive_request.SHOW_SUCCESS_EDIT');
				this.updateDerivation();
			}
			else
				this.snackBarService.showError('access-management.derive_request.SHOW_ERROR_DERIVATION');
		});
	}

	addDerivation(derivation: string): void {
		this.institutionalNetworkReferenceReportService.addDerivation(this.data.referenceId, derivation)
		.subscribe(creationSuccess => {
			if (creationSuccess) {
				this.snackBarService.showSuccess('access-management.derive_request.SHOW_SUCCESS_DERIVATION');
				this.updateDerivation();
			}
			else
				this.snackBarService.showError('access-management.derive_request.SHOW_ERROR_DERIVATION');
		})
	}

	private updateDerivation() {
		this.institutionalNetworkReferenceReportService.getReferenceDetail(this.data.referenceId).subscribe(
			referenceDetails => this.setDerivation(referenceDetails)
		)
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

	private setAppointment(referenceDetails: ReferenceCompleteDataDto["appointment"]){
		if(referenceDetails) {
			const {appointmentId, appointmentStateId, authorFullName, createdOn, date, institution, professionalFullName} = referenceDetails;
			this.referenceAppointment = {appointmentId, appointmentStateId, authorFullName, createdOn, date, institution, professionalFullName};
			this.permissionService.setEditorAppointment(authorFullName, convertDateTimeDtoToDate(createdOn));
		} else {
			this.permissionService.resetEditorAppontment();
		}
	}

	private setDerivation(referenceDetails: ReferenceCompleteDataDto) {
		if(referenceDetails?.forwarding) {
			const { observation, userId, date, type, createdBy, id } = referenceDetails.forwarding;
			this.derivation = observation;
			this.registerDeriveEditor = { createdBy, date: convertDateTimeDtoToDate(date), type, userId, id };
			this.registerDeriveEditor$.next(this.registerDeriveEditor);
		}
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
