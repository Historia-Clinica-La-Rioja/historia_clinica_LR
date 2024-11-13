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

	@Input() pharmaco: PharmacoDetail;
	@Input() disabled = false;
	@Input() showButtons = false;

	@Output() editEmitter = new EventEmitter<number>();
	@Output() deleteEmitter = new EventEmitter<number>();

	identiferCases = IDENTIFIER_CASES;

	emitEdit(): void {
		this.editEmitter.emit(this.pharmaco.id);
	}

	emitDelete(): void {
		this.deleteEmitter.emit(this.pharmaco.id);
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
	healthProblem?: string
}
