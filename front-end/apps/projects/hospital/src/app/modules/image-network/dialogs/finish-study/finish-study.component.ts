import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { TranslateService } from '@ngx-translate/core';
import { StudyStatusPopupComponent } from '../study-status-popup/study-status-popup.component';
import { AppointmentsService } from "@api-rest/services/appointments.service";
import { ApiErrorMessageDto, DetailsOrderImageDto } from "@api-rest/api-model";
import { APPOINTMENT_STATES_ID } from "@turnos/constants/appointment";

import {catchError, switchMap, tap} from 'rxjs/operators';
import { EMPTY } from "rxjs";
import { SnackBarService } from "@presentation/services/snack-bar.service";
import { processErrors } from "@core/utils/form.utils";

@Component({
	selector: 'app-finish-study',
	templateUrl: './finish-study.component.html',
	styleUrls: ['./finish-study.component.scss']
})
export class FinishStudyComponent implements OnInit {

	observations: string;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: {
			appointmentId: number
		},
		public dialogRef: MatDialogRef<FinishStudyComponent>,
		public translateService: TranslateService,
		private readonly appointmentsService: AppointmentsService,
		private readonly snackBarService: SnackBarService,
		public dialog: MatDialog) {
	}

	ngOnInit(): void {
	}

	confirm() {
		const detailsOrderImage: DetailsOrderImageDto = {
			observations: this.observations
		};
		const appointmentId = this.data.appointmentId;
		const served = APPOINTMENT_STATES_ID.SERVED;

		this.appointmentsService.addStudyObservations(appointmentId, detailsOrderImage)
			.pipe(
				tap(() => this.openStatusDialog(true)),
				switchMap(() =>
					this.appointmentsService.changeStateAppointmentEquipment(appointmentId, served)
						.pipe(
							catchError((error: ApiErrorMessageDto) => {
								processErrors(error, (msg) => this.snackBarService.showError(msg));
								return EMPTY;
							})
						)
				),
				catchError((error: ApiErrorMessageDto) => {
					processErrors(error, (msg) => this.snackBarService.showError(msg));
					this.openStatusDialog(false);
					return EMPTY;
				})
			)
			.subscribe((_) => {
				this.dialogRef.close({ updateState: served });
			});
	}


	openStatusDialog(status) {
		const dialogRef = this.dialog.open(StudyStatusPopupComponent, {
			width: '30%',
			autoFocus: false,
			data: {
				status: status
			}
		});
		dialogRef.afterClosed().subscribe();
	}

	closeDialog() {
		this.dialogRef.close()
	}
}
