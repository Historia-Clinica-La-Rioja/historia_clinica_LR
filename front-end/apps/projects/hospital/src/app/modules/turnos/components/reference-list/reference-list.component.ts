import { Component, Input } from '@angular/core';
import { PageDto, ReferenceReportDto } from '@api-rest/api-model';
import { Report } from '../report-information/report-information.component';
import { ReportCompleteDataPopupComponent } from '@turnos/dialogs/report-complete-data-popup/report-complete-data-popup.component';
import { MatDialog } from '@angular/material/dialog';
import { toReport } from '@turnos/utils/mapper.utils';
import { ReferenceReportFacadeService } from '@turnos/services/reference-report-facade.service';

const PAGE_SIZE_OPTIONS = [5, 10, 25];

@Component({
	selector: 'app-reference-list',
	templateUrl: './reference-list.component.html',
	styleUrls: ['./reference-list.component.scss']
})
export class ReferenceListComponent {

	referenceReports: Report[] = [];
	pageSizeOptions = PAGE_SIZE_OPTIONS;
	totalElementsAmount: number;

	@Input()
	set reports(list: PageDto<ReferenceReportDto>) {
		this.referenceReports = (list?.content) ? list?.content.map(report => toReport(report)) : [];
		this.totalElementsAmount = list?.totalElementsAmount;
	};

	constructor(
		private readonly dialog: MatDialog,
		readonly referenceReportFacadeService: ReferenceReportFacadeService,
	) { }

	openRequest(referenceId: number) {
		this.dialog.open(ReportCompleteDataPopupComponent, {
			data: {
				referenceId
			},
			autoFocus: false,
			disableClose: true,
			width: '40%',
		});
	}

}
