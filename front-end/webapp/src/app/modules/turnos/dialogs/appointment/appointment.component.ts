import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { NewAttentionComponent } from '../new-attention/new-attention.component';
import { ConfirmDialogComponent } from '@core/dialogs/confirm-dialog/confirm-dialog.component';
import { AppointmentsService } from '@api-rest/services/appointments.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { APPOINTMENT_STATES_ID, CANCEL_STATE_ID, getAppointmentState } from '../../constants/appointment';
import { Router } from '@angular/router';
import { ContextService } from '@core/services/context.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AppointmentDto } from '@api-rest/api-model';

@Component({
	selector: 'app-appointment',
	templateUrl: './appointment.component.html',
	styleUrls: ['./appointment.component.scss']
})
export class AppointmentComponent implements OnInit {

	public appointment: AppointmentDto;
	public formMotivo: FormGroup;
	public estadoSelected: APPOINTMENT_STATES_ID;
	public motivoRequired: boolean = false;
	public institutionId = this.contextService.institutionId;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: PatientAppointmentInformation,
		public dialogRef: MatDialogRef<NewAttentionComponent>,
		private readonly dialog: MatDialog,
		private appointmentService: AppointmentsService,
		private snackBarService: SnackBarService,
		private readonly router: Router,
	 	private readonly contextService: ContextService,
		private readonly formBuilder: FormBuilder,
	) {
	}

	ngOnInit(): void {
		this.formMotivo = this.formBuilder.group({
			motivo: ['', Validators.required]
		});

		this.estadoSelected = this.data.appointmentStateId;

		this.appointmentService.get(this.data.appointmentId)
			.subscribe(appointment => {
				this.appointment = appointment;
				if (this.appointment.stateChangeReason) {
					this.motivoRequired = true;
					this.formMotivo.controls.motivo.setValue(this.appointment.stateChangeReason);
				}
			});
	}

	cancelAppointment() {

		const dialogRefCancelAppointment = this.dialog.open(ConfirmDialogComponent, {
			data: {
				title: 'turnos.cancel.TITLE',
				content: 'turnos.cancel.CONFIRM',
				okButtonLabel: 'buttons.CONFIRM',
			}
		});
		dialogRefCancelAppointment.afterClosed().subscribe(cancelAppointmentAction => {
			if (cancelAppointmentAction) {
				this.appointmentService.changeState(this.data.appointmentId, CANCEL_STATE_ID)
					.subscribe(() => {
						this.dialogRef.close('appointmentCanceled');
						this.snackBarService.showSuccess('turnos.cancel.SUCCESS');
					}, (error) => {
					});
			}
		});
	}

	changeState(newStateId: APPOINTMENT_STATES_ID) {
		if (newStateId !== this.estadoSelected && newStateId !== this.data.appointmentStateId) {
			this.motivoRequired = false;
			this.estadoSelected = newStateId;
			this.submitNewState(newStateId);
		}
	}

	private submitNewState(newStateId: APPOINTMENT_STATES_ID, motivo?: string) {
		this.appointmentService.changeState(this.data.appointmentId, newStateId, motivo)
			.subscribe(() => {
				this.dialogRef.close('statuschanged');
				this.snackBarService.showSuccess(`Estado de turno actualizado a ${getAppointmentState(newStateId).description} exitosamente`);
			}, (error) => {
				this.estadoSelected = this.data.appointmentStateId;
				this.snackBarService.showError(`Error al actualizar estado de turno ${getAppointmentState(this.data.appointmentStateId).description} a ${getAppointmentState(newStateId).description}`);
			});
	}

	setAbsent(): void {
		this.motivoRequired = true;
		this.estadoSelected = APPOINTMENT_STATES_ID.ABSENT;
	}

	saveAbsent(): void {
		if (this.formMotivo.valid) {
			this.submitNewState(APPOINTMENT_STATES_ID.ABSENT, this.formMotivo.value.motivo);
		}
	}

	isCancelable(): boolean {
		return this.data.appointmentStateId === APPOINTMENT_STATES_ID.ASSIGNED || this.data.appointmentStateId === APPOINTMENT_STATES_ID.CONFIRMED;
	}
}

export interface PatientAppointmentInformation {
	patient: {
		id: number,
		fullName: string
		identificationNumber: string,
		phoneNumber: number,
	};
	appointmentId: number;
	appointmentStateId: number;
	date: Date;
	medicalCoverage: {
		name: string,
		affiliateNumber: string
	};
}
