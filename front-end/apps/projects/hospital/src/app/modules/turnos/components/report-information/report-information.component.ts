import { Component, Input } from '@angular/core';
import { ReferenceReportDto } from '@api-rest/api-model';
import { ColoredIconText } from '@presentation/components/colored-icon-text/colored-icon-text.component';

@Component({
	selector: 'app-report-information',
	templateUrl: './report-information.component.html',
	styleUrls: ['./report-information.component.scss']
})
export class ReportInformationComponent {

	@Input() report: Report;

}

export interface Report {
	dto: ReferenceReportDto;
	priority: string;
	coloredIconText: ColoredIconText
}
