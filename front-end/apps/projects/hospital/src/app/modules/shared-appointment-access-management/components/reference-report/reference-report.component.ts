import { Component, OnInit } from '@angular/core';
import { DashboardService } from '@access-management/services/dashboard.service';

@Component({
	selector: 'app-reference-report',
	templateUrl: './reference-report.component.html',
	styleUrls: ['./reference-report.component.scss']
})
export class ReferenceReportComponent implements OnInit {

	constructor(
		readonly dashboardService: DashboardService,
	) { }

	ngOnInit(): void {
		this.dashboardService.initializeService();
	}

}



