import { Component, EventEmitter, Input, Output } from '@angular/core';
import { TriageAdministrativeDto } from '@api-rest/api-model';

@Component({
	selector: 'app-administrative-triage',
	templateUrl: './administrative-triage.component.html',
	styleUrls: ['./administrative-triage.component.scss']
})
export class AdministrativeTriageComponent {

	@Input() confirmLabel = 'Confirmar episodio';
	@Input() cancelLabel = 'Volver';
	@Input() disableConfirmButton: boolean;
	@Output() confirm = new EventEmitter();
	@Output() cancel = new EventEmitter();
	private triageCategoryId: number;
	private doctorsOfficeId: number;

	constructor() {
	}

	setTriageCategoryId(triageCategoryId: number): void {
		this.triageCategoryId = triageCategoryId;
	}

	setDoctorsOfficeId(doctorsOfficeId: number): void {
		this.doctorsOfficeId = doctorsOfficeId;
	}

	confirmTriage(): void {
		const triage: TriageAdministrativeDto = {
			categoryId: this.triageCategoryId,
			doctorsOfficeId: this.doctorsOfficeId
		};
		this.confirm.emit(triage);
	}

	back(): void {
		this.cancel.emit();
	}
}
