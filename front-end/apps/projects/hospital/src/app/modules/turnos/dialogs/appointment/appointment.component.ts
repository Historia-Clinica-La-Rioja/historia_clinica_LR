import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { NewAttentionComponent } from '../new-attention/new-attention.component';
import { AppointmentsService } from '@api-rest/services/appointments.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { APPOINTMENT_STATES_ID, getAppointmentState, MAX_LENGTH_MOTIVO } from '../../constants/appointment';
import { ContextService } from '@core/services/context.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AppointmentDto, PatientMedicalCoverageDto } from '@api-rest/api-model';
import { AppFeature } from '@api-rest/api-model';
import { ERole } from '@api-rest/api-model';
import { CancelAppointmentComponent } from '../cancel-appointment/cancel-appointment.component';
import { getError, hasError, processErrors } from '@core/utils/form.utils';
import { AppointmentsFacadeService } from '../../services/appointments-facade.service';
import { MapperService } from '@core/services/mapper.service';
import {
	determineIfIsHealthInsurance,
	HealthInsurance, MedicalCoverage,
	MedicalCoverageComponent, PatientMedicalCoverage, PrivateHealthInsurance
} from '@core/dialogs/medical-coverage/medical-coverage.component';
import { map, take } from 'rxjs/operators';
import { PatientMedicalCoverageService } from '@api-rest/services/patient-medical-coverage.service';
import { PermissionsService } from '@core/services/permissions.service';
import { Observable } from 'rxjs';
import { FeatureFlagService } from "@core/services/feature-flag.service";

const TEMPORARY_PATIENT = 3;
const ROLES_TO_CHANGE_STATE: ERole[] = [ERole.ADMINISTRATIVO, ERole.ESPECIALISTA_MEDICO, ERole.PROFESIONAL_DE_SALUD, ERole.ENFERMERO];
const ROLES_TO_EDIT: ERole[]
	= [ERole.ADMINISTRATIVO, ERole.ESPECIALISTA_MEDICO, ERole.PROFESIONAL_DE_SALUD, ERole.ENFERMERO];
@Component({
	selector: 'app-appointment',
	templateUrl: './appointment.component.html',
	styleUrls: ['./appointment.component.scss']
})
export class AppointmentComponent implements OnInit {

	readonly appointmentStatesIds = APPOINTMENT_STATES_ID;
	readonly TEMPORARY_PATIENT = TEMPORARY_PATIENT;
	getAppointmentState = getAppointmentState;
	getError = getError;
	hasError = hasError;
	medicalCoverageId: number;

	appointment: AppointmentDto;
	estadoSelected: APPOINTMENT_STATES_ID;
	formMotivo: FormGroup;
	formEdit: FormGroup;
	institutionId = this.contextService.institutionId;
	coverageText: string;
	coverageNumber: any;
	coverageData: PatientMedicalCoverage;
	hasRoleToChangeState$: Observable<boolean>;
	hasRoleToEditPhoneNumber$: Observable<boolean>;
	patientMedicalCoverages: PatientMedicalCoverage[];

	public hideFilterPanel = false;
	isEnableDowndloadReportButton = false;
	downdLoadReportIsEnabled: boolean;

	constructor(
		@Inject(MAT_DIALOG_DATA) public appointmentData: PatientAppointmentInformation,
		public dialogRef: MatDialogRef<NewAttentionComponent>,
		private readonly dialog: MatDialog,
		private readonly appointmentService: AppointmentsService,
		private readonly snackBarService: SnackBarService,
		private readonly contextService: ContextService,
		private readonly formBuilder: FormBuilder,
		private readonly appointmentFacade: AppointmentsFacadeService,
		private readonly mapperService: MapperService,
		private readonly patientMedicalCoverageService: PatientMedicalCoverageService,
		private readonly permissionsService: PermissionsService,
		private readonly featureFlagService: FeatureFlagService,

	) {
		this.featureFlagService.isActive(AppFeature.HABILITAR_INFORMES_TURNOS).subscribe(isOn => this.downdLoadReportIsEnabled = isOn);
	}

	ngOnInit(): void {

		this.formMotivo = this.formBuilder.group({
			motivo: ['', [Validators.required, Validators.maxLength(MAX_LENGTH_MOTIVO)]]
		});

		this.formEdit = this.formBuilder.group({
			//Medical Coverage selected in Edit Mode
			newCoverageData: null,
			phoneNumber: null
		});

		this.setMedicalCoverages();
		this.formEdit.controls.phoneNumber.setValue(this.appointmentData.phoneNumber);
		this.appointmentService.get(this.appointmentData.appointmentId)
			.subscribe(appointment => {
				this.appointment = appointment;
				this.estadoSelected = this.appointment?.appointmentStateId;
				if (this.appointment.stateChangeReason) {
					this.formMotivo.controls.motivo.setValue(this.appointment.stateChangeReason);
				}
				if (this.appointment.patientMedicalCoverageId) {
					this.patientMedicalCoverageService.getPatientMedicalCoverage(this.appointment.patientMedicalCoverageId)
						.pipe(
							map(
								s => this.patientMedicalCoverages.find(mc => mc.id === s.id)
							)
						)
						.subscribe(coverageData => {
							if (coverageData) {
								this.coverageData = coverageData;
								this.formEdit.controls.newCoverageData.setValue(coverageData);
								this.setCoverageText(this.formEdit.controls.newCoverageData.value);
							}
						});
				}
			});

		this.hasRoleToChangeState$ = this.permissionsService.hasContextAssignments$(ROLES_TO_CHANGE_STATE).pipe(take(1));

		this.hasRoleToEditPhoneNumber$ = this.permissionsService.hasContextAssignments$(ROLES_TO_EDIT).pipe(take(1));
	}

	private setMedicalCoverages(): void {
		this.patientMedicalCoverageService.getActivePatientMedicalCoverages(Number(this.appointmentData.patient.id))
			.pipe(
				map(
					patientMedicalCoveragesDto =>
						patientMedicalCoveragesDto.map(s => this.mapperService.toPatientMedicalCoverage(s))
				)
			)
			.subscribe((patientMedicalCoverages: PatientMedicalCoverage[]) => this.patientMedicalCoverages = patientMedicalCoverages);
	}

	changeState(newStateId: APPOINTMENT_STATES_ID): void {
		this.estadoSelected = newStateId;
	}

	onClickedState(newStateId: APPOINTMENT_STATES_ID): void {
		if (this.estadoSelected !== newStateId) {
			this.changeState(newStateId);
			if (this.isANewState(newStateId) && !this.isMotivoRequired()) {
				this.submitNewState(newStateId);
			}
		}
	}

	private isANewState(newStateId: APPOINTMENT_STATES_ID) {
		return newStateId !== this.appointment?.appointmentStateId;
	}

	cancelAppointment(): void {
		const dialogRefCancelAppointment = this.dialog.open(CancelAppointmentComponent, {
			data: this.appointmentData.appointmentId
		});
		dialogRefCancelAppointment.afterClosed().subscribe(canceledAppointment => {
			if (canceledAppointment) {
				this.closeDialog('statuschanged');
			}
		});
	}

	saveAbsent(): void {
		if (this.formMotivo.valid) {
			this.submitNewState(APPOINTMENT_STATES_ID.ABSENT, this.formMotivo.value.motivo);
		}
	}

	edit(): void {
		if (this.formEdit.valid) {
			if (this.isAssigned()
				&& this.formEdit.controls.newCoverageData.dirty
				&& this.formEdit.controls.newCoverageData.value) {
				const patientMedicalCoverageId = this.formEdit.controls.newCoverageData.value.id;
				this.coverageData = this.formEdit.controls.newCoverageData.value
				this.updateCoverageData(patientMedicalCoverageId);
				this.setCoverageText(this.formEdit.controls.newCoverageData.value);
			}
			if (this.formEdit.controls.phoneNumber.dirty){
				this.updatePhoneNumber(this.formEdit.controls.phoneNumber.value);
			}
		}
		this.hideFilters();
	}

	isMotivoRequired(): boolean {
		return this.estadoSelected === APPOINTMENT_STATES_ID.ABSENT;
	}

	isAssigned(): boolean {
		return this.appointment?.appointmentStateId === APPOINTMENT_STATES_ID.ASSIGNED;
	}

	isCancelable(): boolean {
		return (this.estadoSelected === APPOINTMENT_STATES_ID.ASSIGNED &&
			this.appointment?.appointmentStateId === APPOINTMENT_STATES_ID.ASSIGNED) ||
			(this.estadoSelected === APPOINTMENT_STATES_ID.CONFIRMED &&
				this.appointment?.appointmentStateId === APPOINTMENT_STATES_ID.CONFIRMED);
	}

	private submitNewState(newStateId: APPOINTMENT_STATES_ID, motivo?: string): void {
		this.appointmentFacade.changeState(this.appointmentData.appointmentId, newStateId, motivo)
			.subscribe(() => {
				this.closeDialog('statuschanged');
				this.snackBarService.showSuccess(`Estado de turno actualizado a ${getAppointmentState(newStateId).description} exitosamente`);
			}, _ => {
				this.changeState(this.appointment?.appointmentStateId);
				this.snackBarService.showError(`Error al actualizar estado de turno
				${getAppointmentState(this.appointment?.appointmentStateId).description} a ${getAppointmentState(newStateId).description}`);
			});
	}

	updatePhoneNumber(phoneNumber: string) {
		this.appointmentFacade.updatePhoneNumber(this.appointmentData.appointmentId, phoneNumber).subscribe(() => {
			this.snackBarService.showSuccess('turnos.appointment.coverageData.UPDATE_SUCCESS');
		}, error => {
			processErrors(error, (msg) => this.snackBarService.showError(msg));
		});
	}

	updateCoverageData(patientMedicalCoverageId: number) {
		this.appointmentService.updateMedicalCoverage(this.appointmentData.appointmentId, patientMedicalCoverageId).subscribe(() => {
			this.snackBarService.showSuccess('turnos.appointment.coverageData.UPDATE_SUCCESS');
		}, error => {
			processErrors(error, (msg) => this.snackBarService.showError(msg));
		});
	}

	private setCoverageText(coverageData) {
		this.coverageNumber = coverageData.affiliateNumber;
		const isHealthInsurance = determineIfIsHealthInsurance(coverageData.medicalCoverage);
		if (isHealthInsurance) {
			let healthInsurance: HealthInsurance;
			healthInsurance = coverageData.medicalCoverage as HealthInsurance;
			this.coverageText = healthInsurance.acronym ?
				healthInsurance.acronym : healthInsurance.name;
		} else {
			let privateHealthInsurance: PrivateHealthInsurance;
			privateHealthInsurance = coverageData.medicalCoverage as PrivateHealthInsurance;
			this.coverageText = privateHealthInsurance.name;
		}
	}

	openMedicalCoverageDialog(): void {
		const dialogRef = this.dialog.open(MedicalCoverageComponent, {
			data: {
				identificationNumber: this.appointmentData.patient.identificationNumber,
				initValues: this.patientMedicalCoverages,
			}
		});

		dialogRef.afterClosed().subscribe(
			values => {
				if (values) {
					const patientCoverages: PatientMedicalCoverageDto[] =
						values.patientMedicalCoverages.map(s => this.mapperService.toPatientMedicalCoverageDto(s));

					this.patientMedicalCoverageService.addPatientMedicalCoverages(Number(this.appointmentData.patient.id), patientCoverages).subscribe(
						_ => {
							this.setMedicalCoverages();
							this.snackBarService.showSuccess('ambulatoria.paciente.ordenes_prescripciones.toast_messages.POST_UPDATE_COVERAGE_SUCCESS');
						},
						_ => this.snackBarService.showError('ambulatoria.paciente.ordenes_prescripciones.toast_messages.POST_UPDATE_COVERAGE_ERROR')
					);
				}
			}
		);
	}

	hideFilters(): void {
		this.hideFilterPanel = !this.hideFilterPanel;
	}

	getFullMedicalCoverageText(patientMedicalCoverage): string {
		const medicalCoverageText = [patientMedicalCoverage.medicalCoverage.acronym, patientMedicalCoverage.medicalCoverage.name, patientMedicalCoverage.affiliateNumber]
			.filter(Boolean).join(' - ');
		return [medicalCoverageText].filter(Boolean).join(' / ');
	}

	closeDialog(returnValue?: string) {
		this.dialogRef.close(returnValue);
	}

	enableDowndloadReportButton(option: boolean) {
		this.isEnableDowndloadReportButton = option;
	}

	getReportAppointment(): void {
		this.appointmentService.getAppointmentReport(this.appointmentData).subscribe();
	}
}

export interface PatientAppointmentInformation {
	patient: {
		id: number,
		fullName?: string
		identificationNumber?: string,
		typeId: number;
	};
	appointmentId: number;
	appointmentStateId: number;
	date: Date;
	phoneNumber: string;
	healthInsurance?: {
		name: string,
		acronym?: string;
	};
	medicalCoverageName: string;
	affiliateNumber: string;
}
