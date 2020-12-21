import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { TriageDto } from "@api-rest/api-model";

@Component({
	selector: 'app-administrative-triage',
	templateUrl: './administrative-triage.component.html',
	styleUrls: ['./administrative-triage.component.scss']
})
export class AdministrativeTriageComponent implements OnInit {

	@Input('confirmLabel') confirmLabel: string = 'Confirmar episodio';
	@Input('cancelLabel') cancelLabel: string = 'Volver';
	@Output() onConfirmEvent = new EventEmitter();
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
		const triage: TriageDto = {
				categoryId: this.triageCategoryId,
				doctorsOfficeId: this.doctorsOfficeId
			};
		this.onConfirmEvent.emit(triage);
	}

	back(): void {

	}
}
