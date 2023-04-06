import {Component, Input, OnInit} from '@angular/core';
import {UIComponentDto} from '@extensions/extensions-model';
import {
	ChartDefinitionService,
	FilterValue,
} from '@extensions/services/chart-definition.service';
import {HttpClient} from "@angular/common/http";
import {ContextService} from "@core/services/context.service";

export interface FilterDefinition {
	member: string;
	operator: string;
}

export interface FilterFormDefinition {
	filter: FilterDefinition;
	type: string;
	label: string;
}

export interface QueryFilters {
	[key: string]: FilterFormDefinition;
}

export interface FilterValues {
	[key: string]: string[];
}

const CUBEJS_CHART_COMPONENT = "cubejs-chart";

@Component({
	selector: 'app-cubejs-dashboard',
	templateUrl: './cubejs-dashboard.component.html',
	styleUrls: ['./cubejs-dashboard.component.scss']
})
export class CubejsDashboardComponent implements OnInit {

	@Input() set content(content: UIComponentDto[]) {
		this.setChartService(content);
		this._content = content;
	}

	@Input() filters: QueryFilters;

	private params: FilterValues = {};
	public _content: UIComponentDto[];
	public chartDefinitionService: ChartDefinitionService;

	constructor(
		private http: HttpClient,
		private context: ContextService,
	) {
		this.chartDefinitionService = new ChartDefinitionService(http, context);
	}

	ngOnInit(): void {
		this.chartDefinitionService.next([]);
	}

	changeValue(key: string, values: string[]) {
		this.params[key] = values;

		let filtersToAdd: FilterValue[] = Object.entries(this.params)
			.map(([key, values]) => ({
				...this.filters[key].filter,
				values,
			}));
		filtersToAdd = filtersToAdd.filter(filter => filter.values);
		this.chartDefinitionService.next(filtersToAdd);
	}

	setChartService(content: UIComponentDto[]) {
		content.forEach(c => {
			if (c.type === CUBEJS_CHART_COMPONENT)
				c.args.service = this.chartDefinitionService;
			else if (c.args.content)
				this.setChartService(<UIComponentDto[]>c.args.content)
		});
	}
}
