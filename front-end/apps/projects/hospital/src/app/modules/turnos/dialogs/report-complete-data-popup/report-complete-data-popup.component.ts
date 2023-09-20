import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ReferenceCompleteDataDto } from '@api-rest/api-model';
import { ReferenceReportService } from '@api-rest/services/reference-report.service';

@Component({
	selector: 'app-report-complete-data-popup',
	templateUrl: './report-complete-data-popup.component.html',
	styleUrls: ['./report-complete-data-popup.component.scss']
})
export class ReportCompleteDataPopupComponent implements OnInit {

	referenceCompleteData: ReferenceCompleteDataDto;
	constructor(
		private readonly referenceReportService: ReferenceReportService,
		@Inject(MAT_DIALOG_DATA) public data,
	) { }

	ngOnInit(): void {
		this.referenceReportService.getReferenceDetail(this.data.referenceId).subscribe(referenceDetails => this.referenceCompleteData = referenceDetails);
	}

}
