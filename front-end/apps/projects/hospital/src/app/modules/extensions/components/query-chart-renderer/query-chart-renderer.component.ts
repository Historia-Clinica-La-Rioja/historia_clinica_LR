import { Component, Input } from '@angular/core';
import { ChartService } from './chart.service';
import { UiChartBo } from '../ui-chart/ui-chart.component';

@Component({
	selector: 'app-query-chart-renderer',
	templateUrl: './query-chart-renderer.component.html',
	styleUrls: ['./query-chart-renderer.component.scss']
})
export class QueryChartRendererComponent {
	chartService: ChartService;


	constructor() { }

	@Input('params')
	set params({ resultSet, chartDefinition, renderConfig }: UiChartBo) {
		this.chartService = new ChartService(
			resultSet,
			chartDefinition,
			renderConfig.chartOptions,
		);
	}

}
