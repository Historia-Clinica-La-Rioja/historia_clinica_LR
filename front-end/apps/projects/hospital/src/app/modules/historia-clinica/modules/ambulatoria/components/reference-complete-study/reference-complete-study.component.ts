import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ReferenceCompleteDataDto, ReferenceRequestDto } from '@api-rest/api-model';
import { PrescriptionStatus, ReferenceCompleteData } from '../reference-request-data/reference-request-data.component';
import { InstitutionalReferenceReportService } from '@api-rest/services/institutional-reference-report.service';
import { Observable, map } from 'rxjs';
import { mapToReferenceCompleteData } from '@access-management/utils/mapper.utils';
import { ReportReference } from '../reference-study-closure-information/reference-study-closure-information.component';

@Component({
	selector: 'app-reference-complete-study',
	templateUrl: './reference-complete-study.component.html',
	styleUrls: ['./reference-complete-study.component.scss']
})
export class ReferenceCompleteStudyComponent implements OnInit {
	_reference$ : Observable<ReferenceCompleteData>;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: {
			reference: ReferenceRequestDto;
			patientId: number,
			referenceId: number,
			diagnosticReportId: number,
			reportReference: ReportReference;
			status: PrescriptionStatus,
			order: number,
		},
		private readonly institutionaReferenceReportService: InstitutionalReferenceReportService,
	) { }

	ngOnInit() {
		const referenceId = this.data.referenceId;
		this._reference$ = this.institutionaReferenceReportService.getReferenceDetail(referenceId).pipe(
			map((ReferenceComplete: ReferenceCompleteDataDto) =>
				mapToReferenceCompleteData(ReferenceComplete.reference)
			)
		);
	}


}
