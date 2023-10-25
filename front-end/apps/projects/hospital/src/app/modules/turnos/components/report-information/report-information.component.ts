import { Component, Input } from '@angular/core';
import { ReferenceReportDto } from '@api-rest/api-model';
import { Color } from '@presentation/colored-label/colored-label.component';
import { ColoredIconText } from '@presentation/components/colored-icon-text/colored-icon-text.component';
import { IDENTIFIER_CASES } from '../../../hsi-components/identifier-cases/identifier-cases.component';

@Component({
	selector: 'app-report-information',
	templateUrl: './report-information.component.html',
	styleUrls: ['./report-information.component.scss']
})
export class ReportInformationComponent {

	identiferCases = IDENTIFIER_CASES;
	@Input() report: Report;

}

export interface Report {
	dto: ReferenceReportDto;
	priority: string;
	state: ReferenceState;
	coloredIconText: ColoredIconText
}

export interface ReferenceState {
	description: string;
	color: Color;
}
