import { Component, Input } from '@angular/core';
import { ReferenceReportDto } from '@api-rest/api-model';
import { Report } from '../report-information/report-information.component';
import { getColoredIconText, getPriority } from '@turnos/utils/reference.utils';

@Component({
	selector: 'app-received',
	templateUrl: './received.component.html',
	styleUrls: ['./received.component.scss']
})
export class ReceivedComponent {

	reports: Report[] = [];

	@Input()
	set received(list: ReferenceReportDto[]) {
		if (list?.length)
			this.reports = list.map(report => {
				return {
					dto: report,
					priority: getPriority(report.priority.description),
					coloredIconText: getColoredIconText(report.closureType)
				}
			});
		else
			this.reports = [];
	};

	constructor() { }

}
