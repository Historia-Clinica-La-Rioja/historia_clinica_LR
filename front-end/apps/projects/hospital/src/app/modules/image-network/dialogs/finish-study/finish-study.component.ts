import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { TranslateService } from '@ngx-translate/core';
import { StudyStatusPopupComponent } from '../study-status-popup/study-status-popup.component';
import { AppointmentsService } from "@api-rest/services/appointments.service";
import { ApiErrorMessageDto, DetailsOrderImageDto } from "@api-rest/api-model";
import { APPOINTMENT_STATES_ID } from "@turnos/constants/appointment";

import {catchError, concatMap, map, switchMap, tap} from 'rxjs/operators';
import { EMPTY, Observable } from "rxjs";
import { SnackBarService } from "@presentation/services/snack-bar.service";
import { processErrors } from "@core/utils/form.utils";
import { PrescripcionesService } from '@historia-clinica/modules/ambulatoria/services/prescripciones.service';
import { MatCheckboxChange } from '@angular/material/checkbox';
import { DetailOrderImage } from '../../components/order-image-detail/order-image-detail.component';

@Component({
	selector: 'app-finish-study',
	templateUrl: './finish-study.component.html',
	styleUrls: ['./finish-study.component.scss']
})
export class FinishStudyComponent  implements OnInit {

	observations: string;
	reportNotRequired = false;
	detailOrderInfo$: Observable<DetailOrderImage>
	private served =  APPOINTMENT_STATES_ID.SERVED;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: StudyInfo,
		public dialogRef: MatDialogRef<FinishStudyComponent>,
		public translateService: TranslateService,
		private readonly appointmentsService: AppointmentsService,
		private readonly snackBarService: SnackBarService,
		private readonly prescriptionService: PrescripcionesService,
		public dialog: MatDialog) {
	}

	ngOnInit(): void {
		this.detailOrderInfo$ = this.appointmentsService.getAppoinmentOrderDetail(this.data.appointmentId, this.data.isTranscribed)
		.pipe(
			map(orderDetail =>{ return { ...orderDetail ,
				studyName: this.data.studyName,
				hasOrder: this.data.hasOrder,
				studiesNames: this.data.studies,
				creationDate:  orderDetail.creationDate ? new Date(orderDetail.creationDate) : null,
				patient:this.data.patient
			}}))
	}

	confirm() {
		const detailsOrderImage: DetailsOrderImageDto = {
			observations: this.observations,
			isReportRequired: !this.reportNotRequired,
		};
		const appointmentId = this.data.appointmentId;

        this.appointmentsService.addStudyObservations(appointmentId, detailsOrderImage)
            .pipe(
                tap(() => this.openStatusDialog('check_circle', 'green', 'image-network.appointments.STUDY_COMPLETED')),
                concatMap(
                    () => this.appointmentsService.changeStateAppointmentEquipment(appointmentId, this.served)
                            .pipe(
                                catchError((error: ApiErrorMessageDto) => {
                                    processErrors(error, (msg) => this.snackBarService.showError(msg));
                                    return EMPTY;
                                }),
								switchMap( _ => this.prescriptionService.completeStudyByRdi(this.data.patientId, this.data.appointmentId) ))
                            ),
                catchError((error: ApiErrorMessageDto) => {
                    processErrors(error, (msg) => this.snackBarService.showError(msg));
                    this.openStatusDialog('cancel', 'red', 'image-network.appointments.STUDY_ERROR');
                    return EMPTY;
                })
            )
            .subscribe((_) => {
                this.dialogRef.close({ updateState: this.served, reportRequired: !this.reportNotRequired });
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
}

export interface StudyInfo {
	hasOrder?: boolean;
	isTranscribed?: boolean;
	studyName?: string;
	appointmentId: number,
	patientId: number,
	patient?: string
	studies?: string;
}