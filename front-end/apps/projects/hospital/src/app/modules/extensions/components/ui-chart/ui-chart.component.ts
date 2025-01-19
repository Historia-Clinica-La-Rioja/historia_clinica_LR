import { Component, Input, OnInit } from '@angular/core';
import { EMPTY, Observable, catchError, map, switchMap, tap } from 'rxjs';

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
	errorCube: boolean = true;

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
				this.errorCube = false;
			}),
			switchMap(({cubeQuery, chartType, pivotConfig}: UiChartDefinitionBo) => this.cubejsClient.load(cubeQuery).pipe(
				catchError(
					e => {
						this.errorCube = true;
						const {a, ...rest} = e;
						console.log('errorCube', rest);
						return EMPTY;
					}
				),
				map((resultSet: ResultSet) => ({
					chartDefinition: { cubeQuery, chartType, pivotConfig },
					resultSet,
					renderConfig: {
						chartOptions: this.chartOptions,
						title: this.title,
					},
				})),
			)),
			tap(() => this.errorCube = false),
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
