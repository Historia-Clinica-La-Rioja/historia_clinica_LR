import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { ReplaySubject, Subscription } from 'rxjs';
import { ChartDefinitionService } from '@extensions/services/chart-definition.service';
import {ChartDefinitionDto, UIComponentDto, UILabelDto} from '@extensions/extensions-model';
import {ChartOptions} from "chart.js";

const toUIComponentDto = (error: any): UIComponentDto => ({
	type: 'json',
	args: {
		content: error
	}
});

@Component({
	selector: 'app-cubejs-chart',
	templateUrl: './cubejs-chart.component.html',
	styleUrls: ['./cubejs-chart.component.scss']
})
export class CubejsChartComponent implements OnDestroy, OnInit {

	@Input() chartOptions?: ChartOptions;
	@Input() title?: UILabelDto;
	@Input() chartDefinitionService: ChartDefinitionService;
	@Input() query: string;
	error: UIComponentDto = undefined;
	chartType = new ReplaySubject<any>(1);
	cubeQuery = new ReplaySubject<any>(1);
	pivotConfig = new ReplaySubject<any>(1);

	private chartDefinitionSubscription: Subscription;

	constructor() {
	}

	ngOnInit(): void {
		if (!this.query) {
			console.warn('Chart with undefined queryStream');
			return;
		}
		this.chartDefinitionSubscription = this.chartDefinitionService.queryStream$(this.query).subscribe(
			(queryStream: ChartDefinitionDto) => {
				this.error = undefined;
				this.chartType.next(queryStream.chartType);
				this.cubeQuery.next(queryStream.cubeQuery);
				this.pivotConfig.next(queryStream.pivotConfig);
			},
			(error: any) => this.error = toUIComponentDto(error),
		)
	}

	ngOnDestroy() {
		this.chartDefinitionSubscription?.unsubscribe();
	}
}
