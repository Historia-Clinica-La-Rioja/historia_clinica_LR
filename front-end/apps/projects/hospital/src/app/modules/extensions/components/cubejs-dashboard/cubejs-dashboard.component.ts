import { Component, Input, OnInit } from '@angular/core';
import { formatDateOnlyISO } from '@core/utils/date.utils';
import { UIComponentDto } from '@extensions/extensions-model';
import { ChartDefinitionService, QueryForm } from '@extensions/services/chart-definition.service';

@Component({
	selector: 'app-cubejs-dashboard',
	templateUrl: './cubejs-dashboard.component.html',
	styleUrls: ['./cubejs-dashboard.component.scss']
})
export class CubejsDashboardComponent implements OnInit {

	@Input() content: UIComponentDto[];

	constructor(
		private chartDefinitionService: ChartDefinitionService,
	) { }

	ngOnInit(): void {
		const queryFormDate = formatDateOnlyISO(new Date());
		this.chartDefinitionService.next({ date: queryFormDate });
	}

}
