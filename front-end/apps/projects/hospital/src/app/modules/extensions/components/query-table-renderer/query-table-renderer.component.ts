import { Component, Input, OnInit } from '@angular/core';
import { flattenColumns, getDisplayedColumns } from '../query-renderer/utils';
import { PivotConfig, ResultSet } from '@cubejs-client/core';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { AppFeature } from '@api-rest/api-model';

@Component({
	selector: 'app-query-table-renderer',
	templateUrl: './query-table-renderer.component.html',
	styleUrls: ['./query-table-renderer.component.scss']
})
export class QueryTableRendererComponent implements OnInit {
	displayedColumns: string[] = [];
	tableData: any[] = [];
	columnTitles: string[] = [];
	nameSelfDeterminationFF: boolean;

	constructor(
		private readonly featureFlagService: FeatureFlagService,
	) {
		this.featureFlagService.isActive(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS).subscribe(isOn => {
			this.nameSelfDeterminationFF = isOn
		});
	}

	ngOnInit(): void {
	}

	@Input('params')
	set params({ resultSet, pivotConfig }: QueryTableParams) {
		this.tableData = resultSet.tablePivot(pivotConfig);

		this.displayedColumns = getDisplayedColumns(
			resultSet.tableColumns(pivotConfig)
		);

		this.columnTitles = flattenColumns(resultSet.tableColumns(pivotConfig));
	}
	

}

export interface QueryTableParams {
	resultSet: ResultSet;
	pivotConfig: PivotConfig;
}
