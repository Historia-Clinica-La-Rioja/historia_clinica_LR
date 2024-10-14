import { Component, Inject, OnInit } from '@angular/core';
import { DateTimeDto } from '@api-rest/api-model';
import { Validators, UntypedFormGroup, UntypedFormBuilder } from '@angular/forms';
import { beforeTimeDateValidation, futureTimeValidation, hasError, TIME_PATTERN } from '@core/utils/form.utils';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { InternmentEpisodeService } from '@api-rest/services/internment-episode.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { dateTimeDtoToStringDate } from '@api-rest/mapper/date-dto.mapper';
import { isEqualDate, toHourMinute } from '@core/utils/date.utils';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { isSameDay } from 'date-fns';
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
	minDate: Date;
	public dischargeForm: UntypedFormGroup;
	public minMedicalDischargeDate: Date;
	public dischargeTypes: {};
	public formSubmited: boolean;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: any,
		private readonly dialogRef: MatDialogRef<MedicalDischargeComponent>,
		private readonly formBuilder: UntypedFormBuilder,
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly intermentEpisodeService: InternmentEpisodeService,
		private readonly snackBarService: SnackBarService,
	) { }

	ngOnInit(): void {
		this.internacionMasterDataService.getDischargeType()
			.subscribe(dischargeTypes => this.dischargeTypes = dischargeTypes);

		this.dischargeForm = this.formBuilder.group({
			date: [this.todayDate, [Validators.required]],
			time: [toHourMinute(this.todayDate)],
			dischargeTypeId: [null, [Validators.required]]
		});


		this.intermentEpisodeService.getLastUpdateDateOfInternmentEpisode(this.data.internmentEpisodeId)
			.subscribe((lastUpdateDate: DateTimeDto) => {
				this.minMedicalDischargeDate = new Date(dateTimeDtoToStringDate(lastUpdateDate));
				this.minTime = toHourMinute(this.minMedicalDischargeDate);
				this.minDate = this.minMedicalDischargeDate;
				this.setValidators();
			});


		this.dischargeForm.get('date').valueChanges.subscribe((value: Date) => {
			if (isSameDay(value, this.todayDate)) {
				if (isEqualDate(this.minDate, new Date())) {
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

	selectedDate(date: Date) {
		this.dischargeForm.controls.date.setValue(date)
	}

	setValidators(): void {
		if (isEqualDate(this.minDate, new Date())){
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
