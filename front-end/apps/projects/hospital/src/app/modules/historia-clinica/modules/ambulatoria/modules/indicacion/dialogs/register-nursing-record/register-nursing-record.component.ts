import { ENursingRecordStatus } from '@api-rest/api-model';
import { DateTimeDto } from '@api-rest/api-model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { dateTimeDtotoLocalDate, dateToDateTimeDtoUTC } from '@api-rest/mapper/date-dto.mapper';
import { differenceInDays, setHours } from 'date-fns';
import { DatePipe } from '@angular/common';
import { DatePipeFormat } from '@core/utils/date.utils';
import { beforeTimeDateValidation, futureTimeValidation, TIME_PATTERN } from '@core/utils/form.utils';
import { DocumentActionReasonComponent } from '../../../internacion/dialogs/document-action-reason/document-action-reason.component';

@Component({
	selector: 'app-register-nursing-record',
	templateUrl: './register-nursing-record.component.html',
	styleUrls: ['./register-nursing-record.component.scss'],
})

export class RegisterNursingRecordComponent implements OnInit {

	form: FormGroup;
	today = new Date();
	singleDigit = 9;
	scheduledAdministrationTimeLocalDate: Date;
	TIME_PATTERN = TIME_PATTERN;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: { indicationDate: Date, scheduledAdministrationTime: DateTimeDto },
		private readonly formBuilder: FormBuilder,
		private readonly dialogRef: MatDialogRef<RegisterNursingRecordComponent>,
		private readonly datePipe: DatePipe,
		private readonly dialog: MatDialog
	) { }

	ngOnInit(): void {
		this.scheduledAdministrationTimeLocalDate = this.getScheduledAdministrationTimeLocalDate();
		this.form = this.formBuilder.group({
			time: [this.datePipe.transform(this.scheduledAdministrationTimeLocalDate, DatePipeFormat.SHORT_TIME)],
			date: [this.scheduledAdministrationTimeLocalDate, [Validators.required]],
			observations: [null]
		});
		this.setValidators();
	}

	save() {
		if (this.form.valid) {
			const registerNursingRecord = this.buildCompletedNursingRecord();
			this.dialogRef.close(registerNursingRecord);
		} else this.form.markAllAsTouched();
	}

	reject() {
		const dialogRef = this.dialog.open(DocumentActionReasonComponent, {
			data: {
				title: 'indicacion.nursing-care.dialogs.reject.TITLE',
				subtitle: 'indicacion.nursing-care.dialogs.reject.SUBTITLE',
				titleColor: 'color-warn'
			},
			width: "50vh",
			autoFocus: false,
			disableClose: true
		});

		dialogRef.afterClosed().subscribe((reason: string) => {
			if (reason) {
				const rejectNursingRecord = { status: ENursingRecordStatus.REJECTED, reason };
				this.dialogRef.close(rejectNursingRecord);
			}
		})
	}

	buildCompletedNursingRecord() {
		const date = new Date(this.form.value.date);
		const timeSplit: number[] = this.form.value.time.split(":").map((value: string) => { return Number(value) });
		date.setHours(timeSplit[0], timeSplit[1]);
		return {
			administrationTime: dateToDateTimeDtoUTC(date),
			status: ENursingRecordStatus.COMPLETED,
			reason: this.form.value.observations,
		}
	}

	setValidators() {
		const date = new Date(this.form.value.date);
		const scheduledTimeString: string = this.datePipe.transform(this.scheduledAdministrationTimeLocalDate, DatePipeFormat.SHORT_TIME);
		if (differenceInDays(this.data.indicationDate, this.today) >= 0)
			this.form.controls.time.setValidators([Validators.required, beforeTimeDateValidation(scheduledTimeString), futureTimeValidation]);
		else
			this.form.controls.time.setValidators([Validators.required, beforeTimeDateValidation(scheduledTimeString)]);
		this.form.controls.time.updateValueAndValidity();
	}

	private getScheduledAdministrationTimeLocalDate(): Date {
		if (!this.data.scheduledAdministrationTime) {
			return setHours(this.data.indicationDate, 0);
		}
		return dateTimeDtotoLocalDate(this.data.scheduledAdministrationTime);
	}

}
