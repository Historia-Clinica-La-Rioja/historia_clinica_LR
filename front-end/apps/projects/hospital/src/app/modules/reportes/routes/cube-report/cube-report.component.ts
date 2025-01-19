import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable, map } from 'rxjs';

@Component({
	selector: 'app-cube-report',
	templateUrl: './cube-report.component.html',
	styleUrls: ['./cube-report.component.scss']
})
export class CubeReportComponent implements OnInit {
	reportName$: Observable<string>;

	constructor(
		private readonly route: ActivatedRoute,
	) { }

	ngOnInit(): void {
		this.reportName$ = this.route.url.pipe(
			map( url => url.map(u => u.path).join('/') ),
		);

	}

}
