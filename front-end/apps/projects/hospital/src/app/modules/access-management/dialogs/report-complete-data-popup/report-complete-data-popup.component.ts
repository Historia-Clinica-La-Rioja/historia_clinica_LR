import { Component, EventEmitter, Inject, OnInit, Output } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef, } from '@angular/material/dialog';
import { EReferenceRegulationState, ERole, ReferenceAppointmentDto, ReferenceCompleteDataDto, ReferenceDataDto, ReferenceRegulationDto } from '@api-rest/api-model';
import { InstitutionalReferenceReportService } from '@api-rest/services/institutional-reference-report.service';
import { ContactDetails } from '@access-management/components/contact-details/contact-details.component';
import { PatientSummary } from '../../../hsi-components/patient-summary/patient-summary.component';
import { BehaviorSubject, Observable, of, switchMap, take } from 'rxjs';
import { AppointmentSummary } from '@access-management/components/appointment-summary/appointment-summary.component';
import { APPOINTMENT_STATES_ID } from '@turnos/constants/appointment';
import { toPatientSummary, toContactDetails, toAppointmentSummary } from '@access-management/utils/mapper.utils';
import { PENDING, PENDING_ATTENTION_STATE, REFERENCE_STATES } from '@access-management/constants/reference';
import { ContextService } from '@core/services/context.service';
import { NO_INSTITUTION } from '../../../home/home.component';
import { InstitutionalNetworkReferenceReportService } from '@api-rest/services/institutional-network-reference-report.service';
import { REGISTER_EDITOR_CASES, RegisterEditor } from '@presentation/components/register-editor-info/register-editor-info.component';
import { PermissionsService } from '@core/services/permissions.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { convertDateTimeDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { DerivationEmmiter, RegisterDerivationEditor } from '../../components/derive-request/derive-request.component'
import { SearchAppointmentsInfoService } from '@access-management/services/search-appointment-info.service';
import { TabsService } from '@access-management/services/tabs.service';


const GESTORES = [ERole.GESTOR_DE_ACCESO_DE_DOMINIO, ERole.GESTOR_DE_ACCESO_LOCAL, ERole.GESTOR_DE_ACCESO_REGIONAL];
const TAB_OFERTA_POR_REGULACION = 1;
const PENDING_STATE = REFERENCE_STATES.PENDING;
const ABSENT_STATE = REFERENCE_STATES.ABSENT;

@Component({
	selector: 'app-report-complete-data-popup',
	templateUrl: './report-complete-data-popup.component.html',
	styleUrls: ['./report-complete-data-popup.component.scss']
})
export class ReportCompleteDataPopupComponent implements OnInit {

	referenceCompleteData: ReferenceCompleteDataDto;
	reportCompleteData: ReportCompleteData;

	colapseContactDetails = false;
	registerEditorAppointment: RegisterEditor;
	referenceAppointment: ReferenceAppointmentDto;
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
	isRoleGestor: boolean;
	hasAppointment = false;
	registerEditorCasesDateHour = REGISTER_EDITOR_CASES.DATE_HOUR;
	pendingAttentionState = PENDING_ATTENTION_STATE;

	@Output() assignTurn: EventEmitter<boolean> = new EventEmitter<boolean>();

	constructor(
		private readonly institutionalReferenceReportService: InstitutionalReferenceReportService,
		private readonly contextService: ContextService,
		private readonly institutionalNetworkReferenceReportService: InstitutionalNetworkReferenceReportService,
		private readonly permissionService: PermissionsService,
		private readonly snackBarService: SnackBarService,
		private readonly searchAppointmentsInfoService: SearchAppointmentsInfoService,
		private readonly tabsService: TabsService,
		private dialogRef: MatDialogRef<ReportCompleteDataPopupComponent>,
		@Inject(MAT_DIALOG_DATA) public data,
	) { }

	ngOnInit(): void {
		this.domainRole = this.contextService.institutionId === NO_INSTITUTION;
		const referenceDetails$ = this.getObservable();
		referenceDetails$.subscribe(
			referenceDetails => {
				this.setAppointment(referenceDetails.appointment);
				this.setObservation(referenceDetails);
				this.setDerivation(referenceDetails);
				this.referenceCompleteData = referenceDetails;
				this.referenceRegulationDto$ = of(this.referenceCompleteData.regulation);
				this.setReportData(this.referenceCompleteData);
				this.colapseContactDetails = this.referenceCompleteData.appointment?.appointmentStateId === APPOINTMENT_STATES_ID.SERVED;
			});
		this.permissionService.hasContextAssignments$(GESTORES).subscribe(hasRole => this.isRoleGestor = hasRole);
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
				this.hasDerivationRequest = true;
			}
			else
				this.snackBarService.showError('access-management.derive_request.SHOW_ERROR_DERIVATION');
		})
	}

	redirectToOfferByRegulation(): void {
		this.searchAppointmentsInfoService.loadInformation(this.reportCompleteData.patient.id, this.reportCompleteData.reference);
		this.tabsService.setTabActive(TAB_OFERTA_POR_REGULACION);
		this.dialogRef.close();
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
			this.registerEditorAppointment = {createdBy: authorFullName, date: convertDateTimeDtoToDate(createdOn)}
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
		this.hasAppointment = this.reportCompleteData.appointment.state.description === PENDING_STATE || this.reportCompleteData.appointment.state.description === ABSENT_STATE;
	}
}

export interface ReportCompleteData {
	patient: PatientSummary;
	contactDetails: ContactDetails;
	reference: ReferenceDataDto;
	appointment: AppointmentSummary;
}
