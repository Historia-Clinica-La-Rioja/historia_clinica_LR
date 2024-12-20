import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommercialMedicationPrescriptionDto } from '@api-rest/api-model';
import { PresentationModule } from '@presentation/presentation.module';
import { IDENTIFIER_CASES, IdentifierCasesComponent } from "../identifier-cases/identifier-cases.component";

@Component({
	selector: 'app-pharmaco-detail',
	templateUrl: './pharmaco-detail.component.html',
	styleUrls: ['./pharmaco-detail.component.scss'],
	standalone: true,
	imports: [PresentationModule, IdentifierCasesComponent]
})
export class PharmacoDetailComponent {

	@Input() set pharmaco(pharmaco: PharmacoDetail) {
		this.pharmacoDetail = pharmaco;
		this.setDefaultTitles(pharmaco.titles);
	}
	pharmacoDetail: PharmacoDetail;
	@Input() disabled = false;
	@Input() showButtons = false;

	@Output() editEmitter = new EventEmitter<number>();
	@Output() deleteEmitter = new EventEmitter<number>();

	identiferCases = IDENTIFIER_CASES;

	emitEdit(): void {
		this.editEmitter.emit(this.pharmacoDetail.id);
	}

	emitDelete(): void {
		this.deleteEmitter.emit(this.pharmacoDetail.id);
	}

	private setDefaultTitles = (titles: PharmacoDetailTitle) => {
		if (titles) return;

		this.pharmacoDetail.titles = {
			unitDose: 'pharmaco-detail.UNIT_DOSE',
			dayDose: 'pharmaco-detail.DAY_DOSE',
			quantity: 'pharmaco-detail.QUANTITY'
		}
	}
}

export interface PharmacoDetail {
	id: number;
	pt: string,
	unitDose: number,
	dayDose: string,
	treatmentDays: string,
	quantity: string,
	interval?: string,
	commercialPt?: string,
	commercialMedicationPrescription?: CommercialMedicationPrescriptionDto,
	observations?: string,
	healthProblem?: string,
	titles?: PharmacoDetailTitle
}

export interface PharmacoDetailTitle {
	unitDose: string,
	dayDose: string,
	quantity: string
}
