import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { TranslateService } from '@ngx-translate/core';
import { ButtonType } from "@presentation/components/button/button.component";
import { StudyStatusPopupComponent } from '../study-status-popup/study-status-popup.component';
import { AppointmentsService } from "@api-rest/services/appointments.service";
import { DetailsOrderImageDto } from "@api-rest/api-model";
import { APPOINTMENT_STATES_ID } from "@turnos/constants/appointment";

import {catchError, map, tap} from 'rxjs/operators';
import { Observable, of } from "rxjs";
import { SnackBarService } from "@presentation/services/snack-bar.service";
import { processErrors } from "@core/utils/form.utils";
import { MatCheckboxChange } from '@angular/material/checkbox';
import { DetailOrderImage } from '../../components/order-image-detail/order-image-detail.component';

@Component({
	selector: 'app-finish-study',
	templateUrl: './finish-study.component.html',
	styleUrls: ['./finish-study.component.scss']
})
export class FinishStudyComponent implements OnInit {

	observations: string;
	reportNotRequired = false;
	detailOrderInfo$: Observable<DetailOrderImage>
	private served =  APPOINTMENT_STATES_ID.SERVED;
	private confirmed =  APPOINTMENT_STATES_ID.CONFIRMED;
	ButtonType = ButtonType;
	isLoading = false;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: StudyInfo,
		public dialogRef: MatDialogRef<FinishStudyComponent>,
		public translateService: TranslateService,
		private readonly snackBarService: SnackBarService,
		private readonly appointmentsService: AppointmentsService,
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

	confirmFinishStudy() {
		const detailsOrderImage: DetailsOrderImageDto = {
			observations: this.observations,
			isReportRequired: !this.reportNotRequired,
			patientId: this.data.patientId,
		};
		const appointmentId = this.data.appointmentId;
		this.isLoading = true;

        this.appointmentsService.finishStudy(appointmentId, detailsOrderImage)
            .pipe(
                tap(() => this.openStatusDialog('check_circle', 'green', 'image-network.appointments.STUDY_COMPLETED')),
                catchError((error) => {
					processErrors(error, (msg: string) => this.snackBarService.showError(msg));
                    this.openStatusDialog('cancel', 'red', 'image-network.appointments.STUDY_ERROR');
                    return of(false);
                })
            )
            .subscribe((result) => {
				this.isLoading = false;
                this.dialogRef.close({
					updateState: result
						? this.served
						: this.confirmed,
					reportRequired: result
						? !this.reportNotRequired
						: this.reportNotRequired
				});
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
