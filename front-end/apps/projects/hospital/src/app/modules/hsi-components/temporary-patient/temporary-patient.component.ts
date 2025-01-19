import { Component, Input } from '@angular/core';
import { ItemSummary, Size } from '@presentation/components/item-summary/item-summary.component';
import { PresentationModule } from '@presentation/presentation.module';

@Component({
	selector: 'app-temporary-patient',
	templateUrl: './temporary-patient.component.html',
	styleUrls: ['./temporary-patient.component.scss'],
	standalone: true,
	imports: [PresentationModule]
})
export class TemporaryPatientComponent {

	@Input() set patientDescription(patientDescription: string) {
		if (patientDescription)
			this.temporaryPatientSummary.subtitle = `Detalle: ${patientDescription}`;
	};
	@Input() size? = Size.SMALL;

	temporaryPatientSummary: ItemSummary = {
		title: "temporary-patient.TITLE",
		avatar: "assets/images/temporary.png"
	}

	constructor() { }

}

