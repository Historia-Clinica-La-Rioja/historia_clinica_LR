import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { TranslateService } from '@ngx-translate/core';
import { StudyStatusPopupComponent } from '../study-status-popup/study-status-popup.component';
import { AppointmentsService } from "@api-rest/services/appointments.service";
import { ApiErrorMessageDto, DetailsOrderImageDto } from "@api-rest/api-model";
import { APPOINTMENT_STATES_ID } from "@turnos/constants/appointment";

import {catchError, concatMap, tap} from 'rxjs/operators';
import { EMPTY } from "rxjs";
import { SnackBarService } from "@presentation/services/snack-bar.service";
import { processErrors } from "@core/utils/form.utils";
import { PrescripcionesService } from '@historia-clinica/modules/ambulatoria/services/prescripciones.service';
import { MatCheckboxChange } from '@angular/material/checkbox';

@Component({
	selector: 'app-finish-study',
	templateUrl: './finish-study.component.html',
	styleUrls: ['./finish-study.component.scss']
})
export class FinishStudyComponent {

	observations: string;
	reportNotRequired = false;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: StudyInfo,
		public dialogRef: MatDialogRef<FinishStudyComponent>,
		public translateService: TranslateService,
		private readonly appointmentsService: AppointmentsService,
		private readonly snackBarService: SnackBarService,
		private readonly prescriptionService: PrescripcionesService,
		public dialog: MatDialog) {
	}

	confirm() {
		const detailsOrderImage: DetailsOrderImageDto = {
			observations: this.observations,
			isReportRequired: !this.reportNotRequired,
		};
		const appointmentId = this.data.appointmentId;
		const served = APPOINTMENT_STATES_ID.SERVED;

        this.appointmentsService.addStudyObservations(appointmentId, detailsOrderImage)
            .pipe(
                tap(() => this.openStatusDialog('check_circle', 'green', 'image-network.appointments.STUDY_COMPLETED')),
                concatMap(
                    () => this.appointmentsService.changeStateAppointmentEquipment(appointmentId, served)
                            .pipe(
                                catchError((error: ApiErrorMessageDto) => {
                                    processErrors(error, (msg) => this.snackBarService.showError(msg));
                                    return EMPTY;
                                })
                            ),
					result => {
						if(result) this.prescriptionService.completeStudyByRdi(this.data.patientId, this.data.appointmentId).subscribe()
					}
                ),
                catchError((error: ApiErrorMessageDto) => {
                    processErrors(error, (msg) => this.snackBarService.showError(msg));
                    this.openStatusDialog('cancel', 'red', 'image-network.appointments.STUDY_ERROR');
                    return EMPTY;
                })
            )
            .subscribe((_) => {
                this.dialogRef.close({ updateState: served });
            });
    }

	changeReportRequirementStatus(event: MatCheckboxChange){
		this.reportNotRequired = event.checked;
	}

	openStatusDialog(icon: string, iconColor: string, popUpMessageTranslate: string) {
		const dialogRef = this.dialog.open(StudyStatusPopupComponent, {
			width: '30%',
			autoFocus: false,
			data: {
				icon,
				iconColor,
				popUpMessageTranslate
			}
		});
		dialogRef.afterClosed().subscribe();
	}

	closeDialog() {
		this.dialogRef.close()
	}
}

export interface StudyInfo {
	appointmentId: number,
	patientId: number,
}