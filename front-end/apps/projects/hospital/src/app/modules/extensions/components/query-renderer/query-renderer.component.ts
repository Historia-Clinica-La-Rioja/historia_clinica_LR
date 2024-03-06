import { Component, Input } from '@angular/core';
import { isQueryPresent, ResultSet } from '@cubejs-client/core';
import { CubejsClient, TChartType } from '@cubejs-client/ngx';
import { BehaviorSubject, combineLatest, of, merge } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';
import { ChartOptions } from 'chart.js';
import { getDisplayedColumns, flattenColumns } from './utils';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { AppFeature } from '@api-rest/api-model';
import { MONTHS_OF_YEAR, } from '@core/utils/moment.utils';
import { isValid, parseISO } from 'date-fns';


const parseDate = (value: string, granularity: string): string => {
	const year = value.slice(0, 4);
	const month = value.slice(5, 7);
	const day = value.slice(8, 10);

	const parseMonth = () => MONTHS_OF_YEAR[Number(month) - 1];
	const parseMonthYear = () => `${month}/${year}`;
	const parseDefault = () => `${day}/${month}/${year}`;

	const parseFunctions = {
		month: parseMonth,
		'month-year': parseMonthYear,
		default: parseDefault,
	};
	const parseFn = parseFunctions[granularity] || parseFunctions.default;
	return parseFn();
};

const isDate = (value: string): boolean => {
	return isValid(parseISO(value))

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

	@Input('chartOptions')
	set ChartOptions(chartOptions: ChartOptions) {
		this.chartOptions = chartOptions || {
			responsive: true,
			maintainAspectRatio: false,
		}
	};

	@Input() title: string
	chartOptions: ChartOptions;
	chartType: any = null;
	isQueryPresent = false;
	error: string | null = null;

	displayedColumns: string[] = [];
	tableData: any[] = [];
	columnTitles: string[] = [];

	chartLabels: string[] = [];
	chartData: any[] = [];

	// attrs only for pie chart
	originalData: any[] = [];
	originalGroupData: any[] = [];
	percentageData: any[] = [];
	percentageGroupData: any[] = [];
	groupChartLabels: string[] = [];

	originalChartLabels: string[];
	nameSelfDeterminationFF: boolean;
	showPercentage: boolean;
	groupSmallData: boolean;
	showGroupSmallData: boolean;
	noData = false;

	numericValues: number[] = [];
	loading = false;

	constructor(
		private readonly featureFlagService: FeatureFlagService,
		private cubejsClient: CubejsClient,
	) {
		this.featureFlagService.isActive(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS).subscribe(isOn => {
			this.nameSelfDeterminationFF = isOn
		});
	}

	ngOnInit() {
		combineLatest({
			isQueryPresent: this.cubeQuery$.pipe(
				switchMap((cubeQuery) => {
					return of(isQueryPresent(cubeQuery || {}));
				})
			),
			resultSet: this.cubeQuery$.pipe(
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
			pivotConfig: this.pivotConfig$,
			chartType: this.chartType$,
		}).subscribe(
			({ isQueryPresent, resultSet, pivotConfig, chartType }:
				{
					isQueryPresent: boolean,
					resultSet: ResultSet,
					pivotConfig: any,
					chartType: TChartType
				}
			) => {
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
			if (this.chartType === 'pie' || this.chartType === 'doughnut') {
				return {
					data: item.series.map(({ value }) => value),
				};
			}

			if (item.title == '  Promedio Sem.') {
				return {
					label: item.title,
					data: item.series.map(({ value }) => value),
					type: 'line',
				};
			}

			if (this.chartType === 'line' || this.chartType === 'bar') {
				let title = (pivotConfig.y.length > 1) ? item.title.charAt(0).toUpperCase() + item.title.slice(1, -5) : item.title;
				return {
					label: title,
					data: item.series.map(({ value }) => value)
				};
			}


			return {
				label: item.title,
				data: item.series.map(({ value }) => value),
				stack: 'a',
			};
		});

		this.chartLabels = resultSet.chartPivot(pivotConfig).map((row) =>
			isDate(row.x) ? parseDate(row.x, this.getGranularityDate(pivotConfig.x[0])) : row.x);

		this.noData = !this.chartData?.length;

		if (!this.noData) {
			if (this.chartType === 'pie' || this.chartType === 'doughnut') {
				this.loadPieData();
			}
		}
	}

	getGranularityDate(xConfig: string): string {
		const x = xConfig.split(".");
		if (x.length > 2)
			return x[2].toString();
		return null;
	}

	loadPieData() {
		this.originalData = [...this.chartData[0].data];
		const pieSum = this.originalData.reduce((partialSum, currentValue) => partialSum + currentValue, 0);
		this.percentageData = [...this.chartData[0].data];
		this.percentageData.forEach((element, i, array) => array[i] = Math.round((element * 100 / pieSum) * 100) / 100);

		this.showPercentage = false;
		this.groupSmallData = false;
		this.originalChartLabels = this.chartLabels;

		const smallDataIndex = this.percentageData.findIndex(x => x < 1);
		this.showGroupSmallData = smallDataIndex > 1;
		if (this.showGroupSmallData) {
			this.percentageGroupData = this.groupedData(this.percentageData, smallDataIndex);
			this.originalGroupData = this.groupedData(this.originalData, smallDataIndex);
			this.groupChartLabels = this.chartLabels.slice(0, smallDataIndex);
			this.groupChartLabels.push('Otros');
		}
	}

	togglePercentage() {
		this.showPercentage = !this.showPercentage;
		if (this.showPercentage) {
			this.chartOptions.plugins.tooltip = {

				callbacks: {
					label: function (context) {
						let label = context.label || ' ';
						if (label) {
							label += ': ';
						}
						label += context.formattedValue + '%'
						return label;
					}
				}
			}
			this.chartData = this.groupSmallData ? [{ data: this.percentageGroupData }] : [{ data: this.percentageData }];
		} else {
			this.chartOptions.plugins.tooltip.callbacks.label = undefined;
			this.chartData = this.groupSmallData ? [{ data: this.originalGroupData }] : [{ data: this.originalData }];
		}
	}

	toggleGroupSmallData() {
		this.groupSmallData = !this.groupSmallData;
		if (this.groupSmallData) {
			this.chartLabels = this.groupChartLabels;
			this.chartData = this.showPercentage ? [{ data: this.percentageGroupData }] : [{ data: this.originalGroupData }];
		}
		else {
			this.chartLabels = this.originalChartLabels;
			this.chartData = this.showPercentage ? [{ data: this.percentageData }] : [{ data: this.originalData }];
		}
	}

	updateTableData(resultSet, pivotConfig) {
		this.displayedColumns = getDisplayedColumns(
			resultSet.tableColumns(pivotConfig)
		);

		this.columnTitles = flattenColumns(resultSet.tableColumns(pivotConfig));

		if (this.nameSelfDeterminationFF) {
			this.deleteColumn('Referencias.paciente');
			this.deleteColumn('Referencias.profesional_solicitante');
			this.deleteColumn('Referencias.profesional_turno');
		}
		else {
			this.deleteColumn('Referencias.paciente_auto_det');
			this.deleteColumn('Referencias.profesional_auto_det');
			this.deleteColumn('Referencias.profesional_turno_auto_det');
		}
	}

	deleteColumn(column) {
		const i = this.displayedColumns.indexOf(column);
		if (i !== -1) {
			this.displayedColumns.splice(i, 1);
			this.columnTitles.splice(i, 1);

			this.tableData.forEach(row => {
				delete row[column];
			});
		}
	}

	formatRow(row) {
		if (row['Referencias.telefono'] === "-")
			row['Referencias.telefono'] = "";
		if (row['Referencias.profesional_turno_auto_det'] === ", ")
			row['Referencias.profesional_turno_auto_det'] = "";
		if (row['Referencias.profesional_turno'] === ", ")
			row['Referencias.profesional_turno'] = "";
		if (row['Referencias.estado_turno'] === "Cancelado") {
			row['Referencias.estado_turno'] = "";
			row['Referencias.fecha_turno'] = "";
			row['Referencias.hora_turno'] = "";
			row['Referencias.institucion_turno'] = "";
			row['Referencias.profesional_turno_auto_det'] = "";
			row['Referencias.profesional_turno'] = "";
		}
	}

	updateNumericData(resultSet) {
		this.numericValues = resultSet
			.seriesNames()
			.map((s) => resultSet.totalRow()[s.key]);
	}

	private groupedData(data: number[], index: number): number[] {
		const others = data.slice(index);
		let othersSum = others.reduce((partialSum, currentValue) => partialSum + currentValue, 0);
		return [...data.slice(0, index), othersSum];
	}
}
