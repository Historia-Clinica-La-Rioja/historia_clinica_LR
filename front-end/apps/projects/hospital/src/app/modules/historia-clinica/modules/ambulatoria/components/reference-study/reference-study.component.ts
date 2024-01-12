import { Component, Input, Output  } from '@angular/core';
import { ReferenceRequestDto } from '@api-rest/api-model';
import { ReportReference } from '../reference-study-closure-information/reference-study-closure-information.component';

@Component({
	selector: 'app-reference-study',
	templateUrl: './reference-study.component.html',
	styleUrls: ['./reference-study.component.scss']
})
export class ReferenceStudyComponent {
	@Input() patientId: number;
	@Input() reference: ReferenceRequestDto;
	@Input() diagnosticReportId: number;
	@Input() reportReference?: ReportReference;
	@Output() reportReferenc?: ReportReference;
}
