import { Component, Input, OnInit } from '@angular/core';
import { SurgicalReportDto } from '@api-rest/api-model';

@Component({
	selector: 'app-surgical-report-prosthesis',
	templateUrl: './surgical-report-prosthesis.component.html',
	styleUrls: ['./surgical-report-prosthesis.component.scss']
})
export class SurgicalReportProsthesisComponent implements OnInit {

	@Input() surgicalReport: SurgicalReportDto;

	prosthesis = true;
	description: string;

	constructor() { }

	ngOnInit(): void {
		this.description = this.surgicalReport.prosthesisDescription;
	}

	changeDescription(description): void {
		this.surgicalReport.prosthesisDescription = description;
	}
}
