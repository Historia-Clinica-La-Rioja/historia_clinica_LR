import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { TriageDto } from "@api-rest/api-model";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";

@Component({
	selector: 'app-adult-gynecological-triage',
	templateUrl: './adult-gynecological-triage.component.html',
	styleUrls: ['./adult-gynecological-triage.component.scss']
})
export class AdultGynecologicalTriageComponent implements OnInit {

	@Input('confirmLabel') confirmLabel: string = 'Confirmar episodio';
	@Input('cancelLabel') cancelLabel: string = 'Volver';
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
		const triage: TriageDto = {
			categoryId: this.triageCategoryId,
			doctorsOfficeId: this.doctorsOfficeId
		};
		this.onConfirm.emit(triage);
	}

	back(): void {
		this.onCancel.emit();
	}
}
