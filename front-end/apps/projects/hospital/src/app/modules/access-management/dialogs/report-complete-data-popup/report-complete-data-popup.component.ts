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
import { RegisterDerivationEditor, RegisterEditor } from '@presentation/components/register-editor-info/register-editor-info.component';
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
	waitingApprovalState = EReferenceRegulationState.WAITING_APPROVAL;
	observation: string;
	derivation: string;
	registerEditor: RegisterEditor = null;
	registerEditor$: BehaviorSubject<RegisterEditor> = new BehaviorSubject<RegisterEditor>(null);
	domainRole = false;
	registerDeriveEditor: RegisterDerivationEditor = null;
	registerDeriveEditor$: BehaviorSubject<RegisterDerivationEditor> = new BehaviorSubject<RegisterDerivationEditor>(null);
	hasObservation: boolean = false;
	hasDerivationRequest = false;
	showDerivationRequest: boolean;


	constructor(
		private readonly institutionalReferenceReportService: InstitutionalReferenceReportService,
		private readonly contextService: ContextService,
		private readonly institutionalNetworkReferenceReportService: InstitutionalNetworkReferenceReportService,
		private readonly permissionService: PermissionsService,
		private readonly snackBarService: SnackBarService,
		@Inject(MAT_DIALOG_DATA) public data,
	) { }

	ngOnInit(): void {
		this.domainRole = this.contextService.institutionId === NO_INSTITUTION;
		const referenceDetails$ = this.getObservable();
		referenceDetails$.subscribe(
			referenceDetails => {
				this.setObservation(referenceDetails);
				this.setDerivation(referenceDetails);
				this.referenceCompleteData = referenceDetails;
				this.referenceRegulationDto$ = of(this.referenceCompleteData.regulation);
				this.setReportData(this.referenceCompleteData);
				this.colapseContactDetails = this.referenceCompleteData.appointment?.appointmentStateId === APPOINTMENT_STATES_ID.SERVED;
			});
		this.permissionService.hasContextAssignments$(GESTORES).subscribe(hasRole => this.showDerivationRequest = hasRole);
	}

	updateApprovalStatus() {
		const referenceDetails$ = this.getObservable();
		this.referenceRegulationDto$ = referenceDetails$.pipe(
			map(referenceDetails => { return referenceDetails.regulation }),
			tap(regulationNewState => this.referenceCompleteData = { ...this.referenceCompleteData, regulation: regulationNewState })
		);
	}

	private getObservable(): Observable<ReferenceCompleteDataDto> {
		return this.domainRole ?
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
					this.hasObservation = true;
				}
				else
					this.snackBarService.showError('turnos.report-complete-data.SHOW_ERROR_OBSERVATION');
			});
	}

	addDerivation(derivation: [string, boolean]): void {
		this.derivation = derivation[0];
		if (derivation[1]) 
			this.institutionalNetworkReferenceReportService.updateDerivation(this.registerDeriveEditor$.getValue().id, derivation[0])
			.subscribe(res => {
				if (res) {
					this.snackBarService.showSuccess('access-management.derive_request.SHOW_SUCCESS_EDIT');
					this.updateDerivation();
				}
				else
					this.snackBarService.showError('access-management.derive_request.SHOW_ERROR_DERIVATION');
			});
		else this.institutionalNetworkReferenceReportService.addDerivation(this.data.referenceId, derivation[0])
		.subscribe(res => {
			if (res) {
				this.snackBarService.showSuccess('access-management.derive_request.SHOW_SUCCESS_DERIVATION');
				this.updateDerivation();
				this.hasDerivationRequest = true;
			}
			else
				this.snackBarService.showError('access-management.derive_request.SHOW_ERROR_DERIVATION');
		});
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
