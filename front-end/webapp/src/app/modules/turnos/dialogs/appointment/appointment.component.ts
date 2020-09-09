import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { NewAttentionComponent } from '../new-attention/new-attention.component';
import { AppointmentsService } from '@api-rest/services/appointments.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { APPOINTMENT_STATES_ID, getAppointmentState, MAX_LENGTH_MOTIVO } from '../../constants/appointment';
import { Router } from '@angular/router';
import { ContextService } from '@core/services/context.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AppointmentDto } from '@api-rest/api-model';
import { CancelAppointmentComponent } from '../cancel-appointment/cancel-appointment.component';
import { getError, hasError } from '@core/utils/form.utils';

@Component({
	selector: 'app-appointment',
	templateUrl: './appointment.component.html',
	styleUrls: ['./appointment.component.scss']
})
export class AppointmentComponent implements OnInit {

	readonly appointmentStatesIds = APPOINTMENT_STATES_ID;
	getAppointmentState = getAppointmentState;
	getError = getError;
	hasError = hasError;

	appointment: AppointmentDto;
	estadoSelected: APPOINTMENT_STATES_ID;
	formMotivo: FormGroup;
	institutionId = this.contextService.institutionId;
	phoneNumberEditMode = false;
	phoneNumberForm: FormGroup;
	phoneNumberText: string;
	constructor(
		@Inject(MAT_DIALOG_DATA) public appointmentData: PatientAppointmentInformation,
		public dialogRef: MatDialogRef<NewAttentionComponent>,
		private readonly dialog: MatDialog,
		private readonly appointmentService: AppointmentsService,
		private readonly snackBarService: SnackBarService,
		private readonly router: Router,
		private readonly contextService: ContextService,
		private readonly formBuilder: FormBuilder,
	) {
	}

	ngOnInit(): void {
		this.formMotivo = this.formBuilder.group({
			motivo: ['', [Validators.required, Validators.maxLength(MAX_LENGTH_MOTIVO)]]
		});
		this.phoneNumberForm = this.formBuilder.group({
			phoneNumber: [this.appointmentData.phoneNumber]
		});
		this.phoneNumberText = this.appointmentData.phoneNumber;
		this.appointmentService.get(this.appointmentData.appointmentId)
			.subscribe(appointment => {
				this.appointment = appointment;
				this.estadoSelected = this.appointment?.appointmentStateId;
				if (this.appointment.stateChangeReason) {
					this.formMotivo.controls.motivo.setValue(this.appointment.stateChangeReason);
				}
			});
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
				this.dialogRef.close('statuschanged');
			}
		});
	}

	saveAbsent(): void {
		if (this.formMotivo.valid) {
			this.submitNewState(APPOINTMENT_STATES_ID.ABSENT, this.formMotivo.value.motivo);
		}
	}

	isMotivoRequired(): boolean {
		return this.estadoSelected === APPOINTMENT_STATES_ID.ABSENT;
	}

	isCancelable(): boolean {
		return (this.estadoSelected === APPOINTMENT_STATES_ID.ASSIGNED &&
			this.appointment?.appointmentStateId === APPOINTMENT_STATES_ID.ASSIGNED) ||
			(this.estadoSelected === APPOINTMENT_STATES_ID.CONFIRMED &&
				this.appointment?.appointmentStateId === APPOINTMENT_STATES_ID.CONFIRMED);
	}

	private submitNewState(newStateId: APPOINTMENT_STATES_ID, motivo?: string): void {
		this.appointmentService.changeState(this.appointmentData.appointmentId, newStateId, motivo)
			.subscribe(() => {
				this.dialogRef.close('statuschanged');
				this.snackBarService.showSuccess(`Estado de turno actualizado a ${getAppointmentState(newStateId).description} exitosamente`);
			}, _ => {
				this.changeState(this.appointment?.appointmentStateId);
				this.snackBarService.showError(`Error al actualizar estado de turno
				${getAppointmentState(this.appointment?.appointmentStateId).description} a ${getAppointmentState(newStateId).description}`);
			});
	}

	updatePhoneNumber() {
		this.phoneNumberEditMode = false;
		this.appointmentService.updatePhoneNumber(this.appointmentData.appointmentId, this.phoneNumberForm.controls.phoneNumber.value)
			.subscribe(() => {
				this.snackBarService.showSuccess('El telefono se modificó correctamente');
				this.phoneNumberText = this.phoneNumberForm.controls.phoneNumber.value;
			}
			, () => this.snackBarService.showError('Error')
			);
	}
}

export interface PatientAppointmentInformation {
	patient: {
		id: number,
		fullName: string
		identificationNumber: string,
	};
	appointmentId: number;
	appointmentStateId: number;
	date: Date;
	medicalCoverage: {
		name: string,
		affiliateNumber: string
	};
	phoneNumber: string;
}
