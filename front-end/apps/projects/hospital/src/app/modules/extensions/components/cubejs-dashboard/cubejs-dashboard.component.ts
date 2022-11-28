import { Component, Input, OnInit } from '@angular/core';
import { UIComponentDto } from '@extensions/extensions-model';
import {
	ChartDefinitionService,
	FilterValue,
} from '@extensions/services/chart-definition.service';

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

@Component({
	selector: 'app-cubejs-dashboard',
	templateUrl: './cubejs-dashboard.component.html',
	styleUrls: ['./cubejs-dashboard.component.scss']
})
export class CubejsDashboardComponent implements OnInit {

	@Input() content: UIComponentDto[];
	@Input() filters: QueryFilters;

	private params: FilterValues = {};

	constructor(
		private chartDefinitionService: ChartDefinitionService,
	) { }

	ngOnInit(): void {
		this.chartDefinitionService.next([]);
	}

	changeValue(key: string, values: string[]) {
		this.params[key] = values;

		const filtersToAdd: FilterValue[] = Object.entries(this.params)
			.map(([key, values]) => ({
				...this.filters[key].filter,
				values,
			}));

		this.chartDefinitionService.next(filtersToAdd);
	}

}
