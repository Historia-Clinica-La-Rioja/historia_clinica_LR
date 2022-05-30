import { Component, Inject, OnInit } from '@angular/core';
import { DateTimeDto } from '@api-rest/api-model';
import { Validators, FormGroup, FormBuilder } from '@angular/forms';
import { newMoment } from '@core/utils/moment.utils';
import { Moment } from 'moment';
import * as moment from 'moment';
import { beforeTimeDateValidation, futureTimeValidation, hasError, TIME_PATTERN } from '@core/utils/form.utils';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { InternmentEpisodeService } from '@api-rest/services/internment-episode.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { dateTimeDtoToStringDate } from '@api-rest/mapper/date-dto.mapper';

import { DatePipe } from '@angular/common';
import { DatePipeFormat } from '@core/utils/date.utils';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";


@Component({
	selector: 'app-medical-discharge',
	templateUrl: './medical-discharge.component.html',
	styleUrls: ['./medical-discharge.component.scss']
})
export class MedicalDischargeComponent implements OnInit {
	TIME_PATTERN = TIME_PATTERN;
	hasError = hasError;
	todayDate = new Date();
	minTime: string;
	minDate: string;
	public dischargeForm: FormGroup;
	public minMedicalDischargeDate: Date;
	public dischargeTypes: {};
	public formSubmited: boolean;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: any,
		private readonly dialogRef: MatDialogRef<MedicalDischargeComponent>,
		private readonly formBuilder: FormBuilder,
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly intermentEpisodeService: InternmentEpisodeService,
		private readonly snackBarService: SnackBarService,
		private readonly datePipe: DatePipe,
	) { }

	ngOnInit(): void {
		this.internacionMasterDataService.getDischargeType()
			.subscribe(dischargeTypes => this.dischargeTypes = dischargeTypes);

		this.dischargeForm = this.formBuilder.group({
			date: [moment(), [Validators.required]],
			time: [this.datePipe.transform(this.todayDate, DatePipeFormat.SHORT_TIME)],
			dischargeTypeId: [null, [Validators.required]]
		});


		this.intermentEpisodeService.getLastUpdateDateOfInternmentEpisode(this.data.internmentEpisodeId)
			.subscribe((lastUpdateDate: DateTimeDto) => {
				this.minMedicalDischargeDate = new Date(dateTimeDtoToStringDate(lastUpdateDate));
				this.minTime = this.datePipe.transform(this.minMedicalDischargeDate, DatePipeFormat.SHORT_TIME);
				this.minDate = this.datePipe.transform(this.minMedicalDischargeDate, DatePipeFormat.SHORT_DATE);
				this.setValidators();
			});


		this.dischargeForm.get('date').valueChanges.subscribe((value: Moment) => {
			if (value?.isSame(newMoment(), 'day')) {
				if (this.minDate === (this.datePipe.transform(new Date(), DatePipeFormat.SHORT_DATE))) {
					this.dischargeForm.get('time').setValidators([Validators.required, beforeTimeDateValidation(this.minTime), futureTimeValidation, Validators.pattern(TIME_PATTERN)])
				} else {
					this.dischargeForm.get('time').setValidators([Validators.required, futureTimeValidation, Validators.pattern(TIME_PATTERN)]);
				}
			} else {
				this.dischargeForm.get('time').removeValidators(futureTimeValidation);
			}
			this.dischargeForm.get('time').updateValueAndValidity();
		});
	}

	setValidators(): void {
		if (this.minDate === (this.datePipe.transform(new Date(), DatePipeFormat.SHORT_DATE))) {
			this.dischargeForm.get('time').setValidators([Validators.required, beforeTimeDateValidation(this.minTime), futureTimeValidation, Validators.pattern(TIME_PATTERN)])
		} else {
			this.dischargeForm.get('time').setValidators([Validators.required, futureTimeValidation, Validators.pattern(TIME_PATTERN)]);
		}
	}

	save(): void {
		this.formSubmited = true;
		if (this.dischargeForm.valid) {
			const request = this.dischargeForm.value;
			const newDatetime = new Date(this.dischargeForm.value.date);
			newDatetime.setHours(this.dischargeForm.value.time.split(":")[0]);
			newDatetime.setMinutes(this.dischargeForm.value.time.split(":")[1]);
			newDatetime.setSeconds(0);
			request.medicalDischargeDate = newDatetime.toISOString();
			this.intermentEpisodeService.medicalDischargeInternmentEpisode(request, this.data.internmentEpisodeId)
				.subscribe(response => {
					this.snackBarService.showSuccess('internaciones.discharge.messages.MEDICAL_DISCHARGE_SUCCESS');
					this.dialogRef.close(true);
				}, _ => this.snackBarService.showError('internaciones.discharge.messages.MEDICAL_DISCHARGE_ERROR')
				);
		}
	}
}
