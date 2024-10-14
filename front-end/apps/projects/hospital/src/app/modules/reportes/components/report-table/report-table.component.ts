import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ReportsCubeService } from '@api-rest/services/reports-cube.service';
import { UIComponentDto } from '@extensions/extensions-model';

@Component({
	selector: 'app-report-table',
	templateUrl: './report-table.component.html',
	styleUrls: ['./report-table.component.scss']
})
export class ReportTableComponent implements OnInit {
	@Input() reportName: string;
	@Output() close = new EventEmitter();
	cubeReportData: UIComponentDto;
	isLoadingRequestReport = true;

	constructor(
		private readonly reportsCubeService: ReportsCubeService,
	) { }

	ngOnInit(): void {
		if (!this.reportName) {
			return;
		}
		this.reportsCubeService.getInstitutionReport(this.reportName).subscribe(result => {
			this.cubeReportData = result
			this.isLoadingRequestReport = false
		});
	}

}
