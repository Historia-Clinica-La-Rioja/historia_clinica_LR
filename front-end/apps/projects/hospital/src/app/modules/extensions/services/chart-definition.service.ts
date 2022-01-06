import { Injectable } from '@angular/core';
import { Observable, ReplaySubject } from 'rxjs';
import { switchMap, tap } from 'rxjs/operators';

import { HttpClient } from '@angular/common/http';
import { environment } from '@environments/environment';
import { ChartDefinitionDto } from '@extensions/extensions-model';
import { ContextService } from '@core/services/context.service';

export interface QueryForm {
	[key: string]: string;
}

@Injectable({
	providedIn: 'root'
})
export class ChartDefinitionService {
	private queryForm = new ReplaySubject<QueryForm>(1);

	constructor(
		private http: HttpClient,
		private context: ContextService,
	) { }

	queryStream$(queryName: string): Observable<ChartDefinitionDto> {
		return this.queryForm.asObservable().pipe(
			switchMap(params => this.fetchChartDefinition(queryName, params)),
		);
	}

	next(params: QueryForm): void {
		this.queryForm.next(params);
	}

	public fetchChartDefinition(name: string, params: QueryForm): Observable<ChartDefinitionDto> {
		const url = `${environment.apiBase}/dashboards/charts/${name}`;

		return this.http.get<ChartDefinitionDto>(url, {
			params: {
				...params,
				institutionId: this.context.institutionId
			},
		});
	}

}
