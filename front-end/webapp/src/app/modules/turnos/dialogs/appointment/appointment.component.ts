import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialog } from '@angular/material/dialog';
import { NewAttentionComponent } from '../new-attention/new-attention.component';
import { ConfirmDialogComponent } from '@core/dialogs/confirm-dialog/confirm-dialog.component';
import { AppointmentsService } from '@api-rest/services/appointments.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { CANCEL_STATE_ID } from '../../constants/appointment';
import { Router } from '@angular/router';
import { ContextService } from '@core/services/context.service';

@Component({
	selector: 'app-appointment',
	templateUrl: './appointment.component.html',
	styleUrls: ['./appointment.component.scss']
})
export class AppointmentComponent implements OnInit {

	// todo mock borrar
	turnos = [
		true,
		false,
		false,
		false
	];

	public institutionId = this.contextService.institutionId;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: PatientAppointmentInformation,
		public dialogRef: MatDialogRef<NewAttentionComponent>,
		private readonly dialog: MatDialog,
		private appointmentService: AppointmentsService,
		private snackBarService: SnackBarService,
		private readonly router: Router,
	 	private contextService: ContextService
	) {
	}

	ngOnInit(): void {
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
}

export class PatientAppointmentInformation {
	patient: {
		id: number,
		fullName: string
		identificationNumber: string,
		phoneNumber: number,
	};
	appointmentId: number;
	date: Date;
	medicalCoverage: {
		name: string,
		affiliateNumber: string
	};
}
