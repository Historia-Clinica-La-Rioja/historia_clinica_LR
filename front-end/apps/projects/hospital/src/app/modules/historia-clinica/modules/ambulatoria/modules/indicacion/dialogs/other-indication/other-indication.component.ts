import { dateToDateTimeDtoUTC } from './../../../../../../../api-rest/mapper/date-dto.mapper';
import { Component, Inject, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { OtherIndicationDto } from '@api-rest/api-model';
import { EIndicationStatus, EIndicationType } from '@api-rest/api-model';
import { dateDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { OtherIndicationTypeDto } from '@api-rest/services/internment-indication.service';
import { getMonth, getYear, isSameDay, isToday } from 'date-fns';
import { HOURS_LIST, INTERVALS_TIME, openConfirmDialog, OTHER_FREQUENCY, OTHER_INDICATION_ID } from '../../constants/internment-indications';

@Component({
	selector: 'app-other-indication',
	templateUrl: './other-indication.component.html',
	styleUrls: ['./other-indication.component.scss']
})
export class OtherIndicationComponent implements OnInit {
	form: FormGroup;
	othersIndicatiosType: OtherIndicationTypeDto[];
	intervals = INTERVALS_TIME;
	hoursList = HOURS_LIST;
	otherFrequency = OTHER_FREQUENCY;
	DEFAULT_RADIO_OPTION = '0';
	OtherIndication = OTHER_INDICATION_ID;
	indicationDate: Date;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: { entryDate: Date, actualDate: Date, patientId: number, professionalId: number, othersIndicatiosType: OtherIndicationTypeDto[] },
		private readonly formBuilder: FormBuilder,
		private dialogRef: MatDialogRef<OtherIndicationComponent>,
		private dialog: MatDialog

	) { }

	ngOnInit(): void {
		this.indicationDate = this.data.actualDate;

		this.othersIndicatiosType = this.data.othersIndicatiosType;

		this.form = this.formBuilder.group({
			indicationType: [null, [Validators.required]],
			note: [null, [Validators.required]],
			indication: [null],
			frequencyOption: [this.DEFAULT_RADIO_OPTION],
			interval: [null],
			startTime: [null],
			frequencyHour: [null],
			event: [null]

		});

		this.form.controls.indicationType.valueChanges.subscribe((frequencyOption: number) => {
			if (frequencyOption === this.OtherIndication) {
				this.form.controls.indication.setValidators([Validators.required]);
				this.form.controls.indication.updateValueAndValidity();
			} else {
				this.form.controls.indication.removeValidators([Validators.required]);
				this.form.controls.indication.updateValueAndValidity();
			}
		});

		this.form.controls.interval.valueChanges.subscribe((frequencyOption) => {
			if (frequencyOption === this.otherFrequency.value) {
				this.form.controls.frequencyHour.setValidators([Validators.required]);
				this.form.controls.frequencyHour.updateValueAndValidity();
			} else {
				this.form.controls.frequencyHour.setValue(null);
				this.form.controls.frequencyHour.removeValidators([Validators.required]);
				this.form.controls.frequencyHour.updateValueAndValidity();
			}
		});

		this.form.controls.frequencyOption.valueChanges.subscribe((frequencyOption) => {

			this.removeFormValidators();

			switch (frequencyOption) {
				case '0': { 					
					this.form.controls.interval.reset();
					this.form.controls.startTime.reset();
					this.form.controls.event.reset();
					break;
				}
				case '1': {
					this.form.controls.startTime.reset();

					this.form.controls.interval.setValidators([Validators.required]);
					this.form.controls.interval.updateValueAndValidity();

					this.form.controls.startTime.setValidators([Validators.required]);
					this.form.controls.startTime.updateValueAndValidity();

					this.form.controls.event.reset();

					break;
				}
				case '2': {
					this.form.controls.startTime.setValidators([Validators.required]);
					this.form.controls.startTime.updateValueAndValidity();

					this.form.controls.interval.reset();
					this.form.controls.startTime.reset();
					this.form.controls.event.reset();
					break;
				}
				case '3': {
					this.form.controls.event.setValidators([Validators.required]);
					this.form.controls.event.updateValueAndValidity();

					this.form.controls.interval.reset();
					this.form.controls.startTime.reset();
					break;
				}
			}

		});
	}

	private removeFormValidators(): void {
		this.form.controls.interval.removeValidators([Validators.required]);
		this.form.controls.interval.updateValueAndValidity();

		this.form.controls.startTime.removeValidators([Validators.required]);
		this.form.controls.startTime.updateValueAndValidity();

		this.form.controls.event.removeValidators([Validators.required]);
		this.form.controls.event.updateValueAndValidity();
	}

	clearFilterField(control: AbstractControl): void {
		control.reset();
	}

	submit(): void {
		if (this.form.valid) {
			if (this.form.controls.frequencyHour?.value)
				this.form.controls.interval.setValue(this.form.controls.frequencyHour.value);

			const otherIndicatio = this.toIndicationDto(this.form.value);
			const otherIndicatioDate = dateDtoToDate(otherIndicatio.indicationDate);
			if (!isToday(otherIndicatioDate) && isSameDay(otherIndicatioDate, this.data.actualDate)) {
				openConfirmDialog(this.dialog, otherIndicatioDate).subscribe(confirm => {
					if (confirm === true) {
						this.dialogRef.close(otherIndicatio);
					}
				});
			} else
				this.dialogRef.close(otherIndicatio);

		}
	}

	private toIndicationDto(otherIndicatio: any): OtherIndicationDto {
		const year = getYear(this.indicationDate);
		const month = getMonth(this.indicationDate);
		const day = this.indicationDate.getDate();
		return {
			id: 0,
			patientId: this.data.patientId,
			type: EIndicationType.OTHER_INDICATION,
			status: EIndicationStatus.INDICATED,
			professionalId: this.data.professionalId,
			createdBy: null,
			indicationDate: {
				year,
				month: month + 1,
				day
			},
			createdOn: null,
			otherIndicationTypeId: otherIndicatio.indicationType,
			description: otherIndicatio.note,
			dosage: {
				frequency: (otherIndicatio?.interval) ? (otherIndicatio?.interval) : null,
				diary: true,
				chronic: true,
				duration: 0,
				periodUnit: this.loadPeriodUnit(),
				event: otherIndicatio.event,
				startDateTime: (otherIndicatio?.startTime) ? 
					dateToDateTimeDtoUTC(new Date(year, month, day, otherIndicatio.startTime))
				: null,
			},
			otherType: otherIndicatio.indication
		}
	}

	setIndicationDate(d: Date) {
		this.indicationDate = d;
	}

	private loadPeriodUnit(): string {
		switch (this.form.value.frequencyOption) {
			case ("1"):
				return "h";
			case ("2"):
				return "d";
			case ("3"):
				return "e";
			default:
				return null;
		}
	}

}

