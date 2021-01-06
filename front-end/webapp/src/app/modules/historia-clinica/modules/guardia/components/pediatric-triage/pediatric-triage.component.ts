import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup } from "@angular/forms";
import { TriageDto } from "@api-rest/api-model";

@Component({
	selector: 'app-pediatric-triage',
	templateUrl: './pediatric-triage.component.html',
	styleUrls: ['./pediatric-triage.component.scss']
})
export class PediatricTriageComponent implements OnInit {

	@Input('confirmLabel') confirmLabel: string = 'Confirmar episodio';
	@Input('cancelLabel') cancelLabel: string = 'Volver';
	@Output() onConfirm = new EventEmitter();
	@Output() onCancel = new EventEmitter();
	private triageCategoryId: number;
	private doctorsOfficeId: number;
	pediatricForm: FormGroup;
	bodyTemperatures: any[];
	muscleHypertonyaOptions: any[];
	respiratoryRetractionOptions: any[];

	constructor(private formBuilder: FormBuilder) {
	}

	ngOnInit(): void {
		this.pediatricForm = this.formBuilder.group({
			evaluation: [null],
			bodyTemperatureId: [null],
			cryingExcesive: [null],
			triageCategoryId: [null],
			respiratoryRate: [null],
			respiratoryRetractionId: [null],
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
