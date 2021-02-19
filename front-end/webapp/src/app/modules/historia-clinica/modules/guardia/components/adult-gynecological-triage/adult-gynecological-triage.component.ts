import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TriageAdultGynecologicalDto } from '@api-rest/api-model';
import { dateToDateTimeDto } from '@api-rest/mapper/date-dto.mapper';
import { VITAL_SIGNS } from '@core/constants/validation-constants';
import { hasError } from '@core/utils/form.utils';

@Component({
	selector: 'app-adult-gynecological-triage',
	templateUrl: './adult-gynecological-triage.component.html',
	styleUrls: ['./adult-gynecological-triage.component.scss']
})
export class AdultGynecologicalTriageComponent implements OnInit {

	@Input() confirmLabel = 'Confirmar episodio';
	@Input() cancelLabel = 'Volver';
	@Output() confirm = new EventEmitter();
	@Output() cancel = new EventEmitter();
	private triageCategoryId: number;
	private doctorsOfficeId: number;
	adultGynecologicalForm: FormGroup;
	minValue = VITAL_SIGNS.min_value;
	hasError = hasError;
	constructor(private formBuilder: FormBuilder) {
	}

	ngOnInit(): void {
		this.adultGynecologicalForm = this.formBuilder.group({
			heartRate: [null, Validators.min(this.minValue)],
			respiratoryRate: [null, Validators.min(this.minValue)],
			temperature: [null, Validators.min(this.minValue)],
			bloodOxygenSaturation: [null, Validators.min(this.minValue)],
			systolicBloodPressure: [null, Validators.min(this.minValue)],
			diastolicBloodPressure: [null, Validators.min(this.minValue)],
			evaluation: [null]
		});
	}

	setTriageCategoryId(triageCategoryId: number): void {
		this.triageCategoryId = triageCategoryId;
	}

	setDoctorsOfficeId(doctorsOfficeId: number): void {
		this.doctorsOfficeId = doctorsOfficeId;
	}


	confirmAdultGynecologicalTriage(): void {

		const formValue = this.adultGynecologicalForm.value;
		if (this.adultGynecologicalForm.valid) {
			const triage: TriageAdultGynecologicalDto = {
				categoryId: this.triageCategoryId,
				doctorsOfficeId: this.doctorsOfficeId,
				notes: formValue.evaluation,
				vitalSigns: toVitalSigns(this.adultGynecologicalForm)
			};
			this.confirm.emit(triage);
		}

		function toVitalSigns(form: FormGroup): any {
			const vitalSigns = {};
			const effectiveTime = dateToDateTimeDto(new Date());
			Object.keys(form.controls).forEach((key: string) => {
				if (formValue[key]) {
					vitalSigns[key] = {
						effectiveTime,
						value: formValue[key]
					};
				}
			});
			return Object.keys(vitalSigns).length !== 0 ? vitalSigns : undefined;
		}
	}

	back(): void {
		this.cancel.emit();
	}
}
