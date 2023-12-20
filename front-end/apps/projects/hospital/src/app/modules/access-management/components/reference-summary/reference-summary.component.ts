import { Component, Input, OnInit } from '@angular/core';
import { ReferenceReportDto } from '@api-rest/api-model';
import { Color } from '@presentation/colored-label/colored-label.component';
import { ColoredIconText } from '@presentation/components/colored-icon-text/colored-icon-text.component';
import { IDENTIFIER_CASES } from '../../../hsi-components/identifier-cases/identifier-cases.component';
import { Size } from '@presentation/components/item-summary/item-summary.component';
import { PatientSummary } from '../../../hsi-components/patient-summary/patient-summary.component';

@Component({
	selector: 'app-reference-summary',
	templateUrl: './reference-summary.component.html',
	styleUrls: ['./reference-summary.component.scss']
})
export class ReferenceSummaryComponent implements OnInit {

	identiferCases = IDENTIFIER_CASES;
	size = Size.SMALL;
	@Input() referenceReport: ReferenceReport;
	clinicalSpecialtiesName: string[] = [];

	ngOnInit(): void {
		this.clinicalSpecialtiesName = this.referenceReport.dto.destinationClinicalSpecialties.map(specialty => specialty);
	}
}

export interface ReferenceReport {
	dto: ReferenceReportDto;
	priority: string;
	state: ReferenceState;
	coloredIconText: ColoredIconText;
	patient: PatientSummary;
}

export interface ReferenceState {
	description: string;
	color: Color;
}
