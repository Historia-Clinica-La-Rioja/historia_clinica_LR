import { Injectable } from '@angular/core';
import { Observable, ReplaySubject } from 'rxjs';
import { map, switchMap, tap } from 'rxjs/operators';

import { HttpClient } from '@angular/common/http';
import { environment } from '@environments/environment';
import { ChartDefinitionDto } from '@extensions/extensions-model';
import { ContextService } from '@core/services/context.service';

export interface FilterValue {
	member: string;
	operator: string;
	values: string[];
}

@Injectable({
	providedIn: 'root'
})
export class ChartDefinitionService {
	private queryForm = new ReplaySubject<FilterValue[]>(1);

	constructor(
		private http: HttpClient,
		private context: ContextService,
	) { }

	queryStream$(queryName: string): Observable<ChartDefinitionDto> {
		return this.queryForm.asObservable().pipe(
			switchMap(filtersToAdd => this.fetchChartDefinition(queryName, filtersToAdd)),
		);
	}

	next(filtersToAdd: FilterValue[]): void {
		this.queryForm.next(filtersToAdd);
	}

	private fetchChartDefinition(name: string, filtersToAdd: FilterValue[]): Observable<ChartDefinitionDto> {
		console.log('fetchChartDefinition', filtersToAdd);
		const url = `${environment.apiBase}/dashboards/charts/${name}`;

		return this.http.get<ChartDefinitionDto>(url, {
			params: {
				institutionId: this.context.institutionId
			},
		}).pipe(
			map(definition => {
				const filters = definition.cubeQuery.filters || [];

				definition.cubeQuery.filters = [...filters, ...filtersToAdd];
				return definition;
			})
		);
	}

}
