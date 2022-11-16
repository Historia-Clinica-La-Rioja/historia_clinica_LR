import { Component, Input } from '@angular/core';
import { isQueryPresent, ResultSet } from '@cubejs-client/core';
import { CubejsClient, TChartType } from '@cubejs-client/ngx';
import { BehaviorSubject, combineLatest, of, merge } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';
import { ChartDataSets, ChartOptions } from 'chart.js';
import { Label } from 'ng2-charts';
import { getDisplayedColumns, flattenColumns } from './utils';
import * as moment from "moment";
import { CSVFileDownloadService } from '@extensions/services/csvfile-download.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { AppFeature } from '@api-rest/api-model';

const formatColumnDate = (tableData: any[], column): any[] => {
	const dateFormatter = (x) => !x ? x : moment(x).format('DD/MM/YYYY');
	return tableData.map(row => {
		return {
			...row,
			[column]: dateFormatter(row[column]),
		}
	});
};

const parseIfDate = (value: string): string => {
	return value.substring(0,10);
}

@Component({
	selector: 'app-query-renderer',
	templateUrl: './query-renderer.component.html',
	styleUrls: ['./query-renderer.component.scss'],
})
export class QueryRendererComponent {
	@Input('cubeQuery')
	cubeQuery$: BehaviorSubject<any>;

	@Input('pivotConfig')
	pivotConfig$: any;

	@Input('chartType')
	chartType$: any;

	@Input('dateFormat')
	dateFormat?: any;

	@Input('reverse')
	reverse?: boolean = false;

	@Input('chartOptions')
	chartOptions?: ChartOptions = {
		responsive: true,
		maintainAspectRatio: false,
	};

	@Input('defaultColor')
	defaultColor?: string;

	@Input('showLegend')
	showLegend?: true;

	@Input() listOnTab: string = null;

	chartType: any = null;
	isQueryPresent = false;
	error: string | null = null;

	displayedColumns: string[] = [];
	tableData: any[] = [];
	columnTitles: string[] = [];
	chartData: ChartDataSets[] = [];
	chartLabels: Label[] = [];
	nameSelfDeterminationFF: boolean;

	noFillChartOptions: ChartOptions = {
		responsive: true,
		maintainAspectRatio: false,
		elements: {
			line: {
				fill: false,
			},
		},
	};
	numericValues: number[] = [];
	loading = false;

	constructor(
		private readonly featureFlagService: FeatureFlagService,
		private cubejsClient: CubejsClient,
		private fileDownloadService: CSVFileDownloadService
	) {
		this.featureFlagService.isActive(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS).subscribe(isOn => {
			this.nameSelfDeterminationFF = isOn
		});
	}

	ngOnInit() {
		combineLatest([
			this.cubeQuery$.pipe(
				switchMap((cubeQuery) => {
					return of(isQueryPresent(cubeQuery || {}));
				})
			),
			this.cubeQuery$.pipe(
				switchMap((cubeQuery) => {
					this.error = null;
					if (!isQueryPresent(cubeQuery || {})) {
						return of(null);
					}
					this.loading = true;

					return merge(
						of(null),
						this.cubejsClient.load(cubeQuery).pipe(
							catchError((error) => {
								this.error = error.toString();
								console.error(error);
								return of(null);
							})
						)
					);
				})
			),
			this.pivotConfig$,
			this.chartType$,
		]).subscribe(
			([isQueryPresent, resultSet, pivotConfig, chartType]: [
				boolean,
				ResultSet,
				any,
				TChartType
			]) => {
				this.chartType = chartType;
				this.isQueryPresent = isQueryPresent;

				if (resultSet != null) {
					this.loading = false;
				}

				const { onQueryLoad } =
					window.parent.window['__cubejsPlayground'] || {};
				if (typeof onQueryLoad === 'function') {
					onQueryLoad({
						resultSet,
						error: this.error,
					});
				}

				if (resultSet) {
					if (this.reverse) {
						//resultSet = this.parseReverse(resultSet);
					}
					if (this.chartType === 'table') {
						this.updateTableData(resultSet, pivotConfig);
					} else if (this.chartType === 'number') {
						this.updateNumericData(resultSet);
					} else {
						this.updateChartData(resultSet, pivotConfig);
					}
				}
			}
		);
	}

	updateChartData(resultSet, pivotConfig) {
		this.chartData = resultSet.series(pivotConfig).map((item) => {
			if (item.title == '  Promedio Sem.') {
				return {
					label: item.title,
					data: item.series.map(({ value }) => value),
					type: 'line',
				};
			} else {
				return {
					label: item.title,
					data: item.series.map(({ value }) => value * (this.reverse ? -1 : 1)),
					stack: 'a',
				};
			}
		});
		if (this.defaultColor) {
			this.chartData.forEach(value => {
				value.backgroundColor = this.defaultColor;
				value.hoverBackgroundColor = this.defaultColor;
			});
		}

		this.chartLabels = resultSet.chartPivot(pivotConfig).map((row) => parseIfDate(row.x));
	}

	formatDate(resultSet) {
		const dateFormatter = ({ x }) => moment(x).format(this.dateFormat);
		this.chartLabels = resultSet.chartPivot().map(dateFormatter);
	}

	updateTableData(resultSet, pivotConfig) {
		this.tableData = formatColumnDate(
			resultSet.tablePivot(pivotConfig),
			'Referencias.fecha_consulta'
		);
		this.displayedColumns = getDisplayedColumns(
			resultSet.tableColumns(pivotConfig)
		);

		this.columnTitles = flattenColumns(resultSet.tableColumns(pivotConfig));

		if (this.nameSelfDeterminationFF) {
			this.deleteColumn('Referencias.paciente');
			this.deleteColumn('Referencias.profesional_solicitante');
		}
		else {
			this.deleteColumn('Referencias.paciente_auto_det');
			this.deleteColumn('Referencias.profesional_auto_det');
		}

		if (this.listOnTab) {
			this.fileDownloadService.addTableData(this.listOnTab, this.columnTitles, this.tableData);
		}
	}

	deleteColumn(column) {
		const i = this.displayedColumns.indexOf(column);
		this.displayedColumns.splice(i, 1);
		this.columnTitles.splice(i, 1);

		this.tableData.forEach(row => {
			delete row[column];
		})
	}

	updateNumericData(resultSet) {
		this.numericValues = resultSet
			.seriesNames()
			.map((s) => resultSet.totalRow()[s.key]);
	}
}
