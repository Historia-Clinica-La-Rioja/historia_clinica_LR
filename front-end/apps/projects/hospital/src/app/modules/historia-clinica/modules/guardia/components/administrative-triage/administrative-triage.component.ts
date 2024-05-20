import { Component, EventEmitter, Input, Output } from '@angular/core';
import { TriageAdministrativeDto } from '@api-rest/api-model';
import { Triage } from '../triage/triage.component';


@Component({
	selector: 'app-administrative-triage',
	templateUrl: './administrative-triage.component.html',
	styleUrls: ['./administrative-triage.component.scss']
})
export class AdministrativeTriageComponent {

	private triageData: Triage;

	@Input() confirmLabel = 'Confirmar episodio';
	@Input() cancelLabel = 'Volver';
	@Input() disableConfirmButton: boolean;
	@Input() canAssignNotDefinedTriageLevel: boolean;
	@Output() confirm = new EventEmitter();
	@Output() cancel = new EventEmitter();

	constructor() {
	}

	setTriageData(triageData: Triage) {
		this.triageData = triageData;
	}

	confirmTriage(): void {
		this.disableConfirmButton = true;
		const triage: TriageAdministrativeDto = {
			categoryId: this.triageData.triageCategoryId,
			doctorsOfficeId: this.triageData.doctorsOfficeId,
			reasons: this.triageData.reasons
		};
		this.confirm.emit(triage);
	}

	back(): void {
		this.cancel.emit();
	}
}
