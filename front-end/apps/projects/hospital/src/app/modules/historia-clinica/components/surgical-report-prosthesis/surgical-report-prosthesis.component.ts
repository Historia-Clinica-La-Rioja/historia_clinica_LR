import { Component, EventEmitter, OnInit, Output } from '@angular/core';

@Component({
	selector: 'app-surgical-report-prosthesis',
	templateUrl: './surgical-report-prosthesis.component.html',
	styleUrls: ['./surgical-report-prosthesis.component.scss']
})
export class SurgicalReportProsthesisComponent implements OnInit {

	@Output() descriptionChange = new EventEmitter();

	prosthesis = true;
	description: string;

	constructor() { }

	ngOnInit(): void {
	}

	changeDescription(description): void {
		this.descriptionChange.emit(description);
	}
}
