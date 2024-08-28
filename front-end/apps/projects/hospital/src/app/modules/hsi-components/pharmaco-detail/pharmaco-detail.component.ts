import { Component, Input } from '@angular/core';
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
	identiferCases = IDENTIFIER_CASES;
}

export interface PharmacoDetail {
	pt: string,
	unitDose: number,
	dayDose: number,
	treatmentDays: string,
	quantity: number,
	interval?: string,
	commercialPt?: string,
	commercialMedicationPrescription?: CommercialMedicationPrescriptionDto,
	observations?: string,
	healthProblem?: string
}
