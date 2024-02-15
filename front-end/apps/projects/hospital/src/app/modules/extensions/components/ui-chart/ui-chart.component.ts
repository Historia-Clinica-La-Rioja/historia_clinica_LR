import { Component, Input, OnInit } from '@angular/core';
import { Observable, map, switchMap, tap } from 'rxjs';

import { PivotConfig, Query, ResultSet } from '@cubejs-client/core';
import { CubejsClient } from '@cubejs-client/ngx';
import { ChartOptions } from 'chart.js';

import { ChartDefinitionService } from '@extensions/services/chart-definition.service';
import { UILabelDto, } from '@extensions/extensions-model';

@Component({
	selector: 'app-ui-chart',
	templateUrl: './ui-chart.component.html',
	styleUrls: ['./ui-chart.component.scss']
})
export class UiChartComponent implements OnInit {
	@Input() query: string; // nombre del ChartDefinition
	@Input() chartOptions?: ChartOptions; // gráficos
	@Input() title?: UILabelDto; // indicadores

	@Input() chartDefinitionService: ChartDefinitionService;

	chart$: Observable<UiChartBo>;
	loadingResults: boolean = true;

	constructor(
		private cubejsClient: CubejsClient,
	) { }

	ngOnInit(): void {
		if (!this.query) {
			console.warn('Chart with undefined queryStream');
			return;
		}

		this.chart$ = this.chartDefinitionService.queryStream$(this.query)
		.pipe(
			tap(() => {
				this.loadingResults = true;
			}),
			switchMap(({cubeQuery, chartType, pivotConfig}: UiChartDefinitionBo) => this.cubejsClient.load(cubeQuery).pipe(
				map((resultSet: ResultSet) => ({
					chartDefinition: { cubeQuery, chartType, pivotConfig },
					resultSet,
					renderConfig: {
						chartOptions: this.chartOptions,
						title: this.title,
					},
				})),
			)),
			tap(() => this.loadingResults = false),
		);
	}

}


export interface UiChartBo {
    chartDefinition: UiChartDefinitionBo;
    resultSet: ResultSet;
    renderConfig: UiChartRenderBo;
}

export interface UiChartDefinitionBo {
	chartType: string;
    cubeQuery: Query;
    pivotConfig: PivotConfig;
}

interface UiChartRenderBo {
	chartOptions?: ChartOptions;
	title?: UILabelDto;
}
