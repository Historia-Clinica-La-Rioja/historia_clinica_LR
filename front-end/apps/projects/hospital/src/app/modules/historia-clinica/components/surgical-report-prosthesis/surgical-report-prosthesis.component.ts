import { Component, OnInit } from '@angular/core';

@Component({
	selector: 'app-surgical-report-prosthesis',
	templateUrl: './surgical-report-prosthesis.component.html',
	styleUrls: ['./surgical-report-prosthesis.component.scss']
})
export class SurgicalReportProsthesisComponent implements OnInit {

	prosthesis = true;

	constructor() { }

	ngOnInit(): void {
	}

}
