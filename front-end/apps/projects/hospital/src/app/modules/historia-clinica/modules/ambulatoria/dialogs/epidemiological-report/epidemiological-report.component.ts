import { Component, OnInit } from '@angular/core';

@Component({
	selector: 'app-epidemiological-report',
	templateUrl: './epidemiological-report.component.html',
	styleUrls: ['./epidemiological-report.component.scss']
})
export class EpidemiologicalReportComponent implements OnInit {

	isDengueProblem: boolean;

	constructor() { }

	ngOnInit(): void {


	}

}
