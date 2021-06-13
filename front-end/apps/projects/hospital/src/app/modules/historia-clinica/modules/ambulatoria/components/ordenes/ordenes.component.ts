import {Component, Input, OnInit} from '@angular/core';
import {RequestMasterDataService} from '@api-rest/services/request-masterdata.service';

@Component({
	selector: 'app-ordenes',
	templateUrl: './ordenes.component.html',
	styleUrls: ['./ordenes.component.scss']
})
export class OrdenesComponent implements OnInit {

	public medicamentStatus = null;
	public diagnosticReportsStatus = null;
	public studyCategories = null;
	@Input() patientId: number;

	constructor(
		private readonly requestMasterDataService: RequestMasterDataService,
	) {
	}

	ngOnInit(): void {
		this.requestMasterDataService.medicationStatus().subscribe(status => {
			this.medicamentStatus = status;
		});
		this.requestMasterDataService.diagnosticReportStatus().subscribe(diagnosticReport => {
			this.diagnosticReportsStatus = diagnosticReport;
		});
		this.requestMasterDataService.categories().subscribe(categories => {
			this.studyCategories = categories;
		});
	}

}
