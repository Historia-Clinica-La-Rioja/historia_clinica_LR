import { Component, Input } from '@angular/core';
import { IDENTIFIER_CASES, IdentifierCasesComponent } from '@hsi-components/identifier-cases/identifier-cases.component';
import { Color } from '@presentation/colored-label/colored-label.component';
import { ColoredIconText } from '@presentation/components/colored-icon-text/colored-icon-text.component';
import { PresentationModule } from '@presentation/presentation.module';

@Component({
	selector: 'app-patient-location',
	templateUrl: './patient-location.component.html',
	styleUrls: ['./patient-location.component.scss'],
	standalone: true,
	imports: [IdentifierCasesComponent, PresentationModule]
})
export class PatientLocationComponent {

	coloredIconText: ColoredIconText = {
		icon: 'meeting_room',
		text:'patientLocationComponent.NO_DATA',
		color:Color.GREY
	};
	identiferCases = IDENTIFIER_CASES;

	@Input() patientLocation: PatientLocation ;
	@Input() professionalFullName: string;
}

export interface PatientLocation {
    bedNumber?: string;
    doctorsOffice?: string;
    roomNumber?: string;
    sector?: string;
    shockroom?: string;
}
