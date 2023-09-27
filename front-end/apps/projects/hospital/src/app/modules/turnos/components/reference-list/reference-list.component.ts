import { ChangeDetectorRef, Component, Input } from '@angular/core';
import { ReferenceReportDto } from '@api-rest/api-model';
import { Report } from '../report-information/report-information.component';
import { getColoredIconText, getPriority, getReferenceState } from '@turnos/utils/reference.utils';

@Component({
	selector: 'app-reference-list',
	templateUrl: './reference-list.component.html',
	styleUrls: ['./reference-list.component.scss']
})
export class ReferenceListComponent {

	filteredReferenceReports: Report[] = [];
	referenceReports: Report[] = [];

	@Input()
	set reports(list: ReferenceReportDto[]) {
		if (list?.length)
			this.referenceReports = list.map(report => {
				const state = getReferenceState(report.appointmentStateId);
				return {
					dto: report,
					priority: getPriority(report.priority.id),
					coloredIconText: getColoredIconText(report.closureType),
					state,
				}
			});
		else
			this.referenceReports = [];
	};

	constructor(
		private readonly changeDetectorRef: ChangeDetectorRef
	) { }

	changeView(result: Report[]) {
		this.filteredReferenceReports = result;
		this.changeDetectorRef.detectChanges();
	}

}
