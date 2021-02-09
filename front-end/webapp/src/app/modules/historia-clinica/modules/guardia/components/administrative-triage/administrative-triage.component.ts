import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { TriageAdministrativeDto, TriageDto } from '@api-rest/api-model';

@Component({
	selector: 'app-administrative-triage',
	templateUrl: './administrative-triage.component.html',
	styleUrls: ['./administrative-triage.component.scss']
})
export class AdministrativeTriageComponent implements OnInit {

	@Input() confirmLabel = 'Confirmar episodio';
	@Input() cancelLabel = 'Volver';
	@Output() onConfirm = new EventEmitter();
	@Output() onCancel = new EventEmitter();
	private triageCategoryId: number;
	private doctorsOfficeId: number;

	constructor() {
	}

	ngOnInit(): void {
	}

	setTriageCategoryId(triageCategoryId: number): void {
		this.triageCategoryId = triageCategoryId;
	}

	setDoctorsOfficeId(doctorsOfficeId: number): void {
		this.doctorsOfficeId = doctorsOfficeId;
	}

	confirm(): void {
		const triage: TriageAdministrativeDto = {
				categoryId: this.triageCategoryId,
				doctorsOfficeId: this.doctorsOfficeId
		};
		this.onConfirm.emit(triage);
	}

	back(): void {
		this.onCancel.emit();
	}
}
