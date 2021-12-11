import { Component, Input } from '@angular/core';
import { ReplaySubject } from 'rxjs';
import { ChartDefinitionService } from '@extensions/services/chart-definition.service';
import { ChartDefinitionDto, UIComponentDto } from '@extensions/extensions-model';


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
export class CubejsChartComponent {

	@Input() dateFormat?: string;
	error: UIComponentDto = undefined;
	chartType = new ReplaySubject<any>(1);
	cubeQuery = new ReplaySubject<any>(1);
	pivotConfig = new ReplaySubject<any>(1);

	constructor(
		private chartDefinitionService: ChartDefinitionService,
	) { }

	@Input()
	set query(queryName: string) {
		if (!queryName) {
			console.warn('Chart with undefined queryStream');
			return;
		}

		this.chartDefinitionService.queryStream$(queryName).subscribe(
			(queryStream: ChartDefinitionDto) => {
				this.error = undefined;
				this.chartType.next(queryStream.chartType);
				this.cubeQuery.next(queryStream.cubeQuery);
				this.pivotConfig.next(queryStream.pivotConfig);
			},
			(error: any) => this.error = toUIComponentDto(error),
		);


	}

}
