import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { TriageAdultGynecologicalDto } from '@api-rest/api-model';
import { FormBuilder, FormGroup } from '@angular/forms';
import { dateToDateTimeDto } from '@api-rest/mapper/date-dto.mapper';

@Component({
	selector: 'app-adult-gynecological-triage',
	templateUrl: './adult-gynecological-triage.component.html',
	styleUrls: ['./adult-gynecological-triage.component.scss']
})
export class AdultGynecologicalTriageComponent implements OnInit {

	@Input() confirmLabel = 'Confirmar episodio';
	@Input() cancelLabel = 'Volver';
	@Output() onConfirm = new EventEmitter();
	@Output() onCancel = new EventEmitter();
	private triageCategoryId: number;
	private doctorsOfficeId: number;
	adultGynecologicalForm: FormGroup;

	constructor(private formBuilder: FormBuilder) {
	}

	ngOnInit(): void {
		this.adultGynecologicalForm = this.formBuilder.group({
			heartRate: [null],
			respiratoryRate: [null],
			temperature: [null],
			bloodOxygenSaturation: [null],
			systolicBloodPressure: [null],
			diastolicBloodPressure: [null],
			evaluation: [null]
		});
	}

	setTriageCategoryId(triageCategoryId: number): void {
		this.triageCategoryId = triageCategoryId;
	}

	setDoctorsOfficeId(doctorsOfficeId: number): void {
		this.doctorsOfficeId = doctorsOfficeId;
	}


	confirm(): void {

		const formValue = this.adultGynecologicalForm.value;

		const triage: TriageAdultGynecologicalDto = {
			categoryId: this.triageCategoryId,
			doctorsOfficeId: this.doctorsOfficeId,
			notes: formValue.evaluation,
			vitalSigns: toVitalSigns(this.adultGynecologicalForm)
		};
		this.onConfirm.emit(triage);

		function toVitalSigns(form: FormGroup): any {
			const vitalSigns = {};
			Object.keys(form.controls).forEach((key: string) => {
				if (formValue[key]) {
					vitalSigns[key] = {
						effectiveTime: dateToDateTimeDto(new Date()),
						value: formValue[key]
					};
				}
			});
			return Object.keys(vitalSigns).keys.length !== 0  ? vitalSigns : undefined;
		}
	}

	back(): void {
		this.onCancel.emit();
	}
}
