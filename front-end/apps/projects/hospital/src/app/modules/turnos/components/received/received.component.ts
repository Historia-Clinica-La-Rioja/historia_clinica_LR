import { ChangeDetectorRef, Component, Input } from '@angular/core';
import { ReferenceReportDto } from '@api-rest/api-model';
import { Report } from '../report-information/report-information.component';
import { getColoredIconText, getPriority } from '@turnos/utils/reference.utils';

@Component({
	selector: 'app-received',
	templateUrl: './received.component.html',
	styleUrls: ['./received.component.scss']
})
export class ReceivedComponent {

	filteredReports: Report[] = [];
	reports: Report[] = [];

	@Input()
	set received(list: ReferenceReportDto[]) {
		if (list?.length)
			this.reports = list.map(report => {
				return {
					dto: report,
					priority: getPriority(report.priority.id),
					coloredIconText: getColoredIconText(report.closureType)
				}
			});
		else
			this.reports = [];
	};

	constructor(
		private readonly changeDetectorRef: ChangeDetectorRef
	) { }

	changeView(result: Report[]) {
		this.filteredReports = result;
		this.changeDetectorRef.detectChanges();
	}

}
