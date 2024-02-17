import { Component, Input } from '@angular/core';
import { ResultSet } from '@cubejs-client/core';

@Component({
	selector: 'app-query-card-renderer',
	templateUrl: './query-card-renderer.component.html',
	styleUrls: ['./query-card-renderer.component.scss']
})
export class QueryCardRendererComponent {
	numericValues: number[] = [];
	title: string;

	constructor() { }

	@Input('params')
	set params({ resultSet, title }: QueryCardParams) {
		this.title = title;
		this.numericValues = resultSet
			.seriesNames()
			.map((s) => resultSet.totalRow()[s.key]);
	}

}

export interface QueryCardParams {
	resultSet: ResultSet;
	title: string;
}
