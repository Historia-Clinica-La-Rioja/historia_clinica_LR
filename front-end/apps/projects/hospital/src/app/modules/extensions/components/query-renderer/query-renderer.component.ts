import { Component, Input } from '@angular/core';
import { isQueryPresent, ResultSet } from '@cubejs-client/core';
import { CubejsClient, TChartType } from '@cubejs-client/ngx';
import { BehaviorSubject, combineLatest, of, merge } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';
import { ChartOptions } from 'chart.js';
import { getDisplayedColumns, flattenColumns } from './utils';
import * as moment from "moment";
import { CSVFileDownloadService } from '@extensions/services/csvfile-download.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { AppFeature } from '@api-rest/api-model';
import { buildFullDate, DateFormat, momentParse, newMoment, MONTHS_OF_YEAR } from '@core/utils/moment.utils';


const formatColumnDate = (tableData: any[], columns: string[]): any[] => {
	const dateFormatter = (x) => !x ? x : moment(x).format('DD/MM/YYYY');
	columns.forEach(column => {
		tableData = tableData.map(row => {
			return {
				...row,
				[column]: dateFormatter(row[column]),
			}
		})
	});
	return tableData;
};

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
	return moment(value, moment.ISO_8601, true).isValid();

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
	set ChartOptions(chartOptions: ChartOptions) {
		this.chartOptions = chartOptions || {
			responsive: true,
			maintainAspectRatio: false,
		}
	};

	@Input('defaultColor')
	defaultColor?: string;

	@Input() listOnTab: string = null;

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
		private fileDownloadService: CSVFileDownloadService
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
					data: item.series.map(({ value }) => value * (this.reverse ? -1 : 1)),
				};
			}

			if (item.title == '  Promedio Sem.') {
				return {
					label: item.title,
					data: item.series.map(({ value }) => value),
					type: 'line',
				};
			}

			if (this.chartType === 'line') {
				return {
					label: item.title,
					data: item.series.map(({ value }) => value * (this.reverse ? -1 : 1))
				};
			}

			if (this.chartType === 'bar') {
				return {
					label: item.title.charAt(0).toUpperCase() + item.title.slice(1, -5),
					data: item.series.map(({ value }) => value * (this.reverse ? -1 : 1)),
				};
			}

			return {
				label: item.title,
				data: item.series.map(({ value }) => value * (this.reverse ? -1 : 1)),
				stack: 'a',
			};
		});

		this.chartLabels = resultSet.chartPivot(pivotConfig).map((row) =>
			isDate(row.x) ? parseDate(row.x, this.getGranularityDate(pivotConfig.x[0])) : row.x);

		this.noData = !this.chartData?.length;

		if(!this.noData) {
			if (this.chartType === 'pie' || this.chartType === 'doughnut') {
				this.loadPieData();
			}

			if (this.defaultColor) {
				this.chartData.forEach(value => {
					value.backgroundColor = this.defaultColor;
					value.hoverBackgroundColor = this.defaultColor;
				});
			}
		}
	}

	getGranularityDate(xConfig : string) : string {
		const x = xConfig.split(".");
		if(x.length > 2 )
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
			this.chartData = this.groupSmallData ? [{data: this.percentageGroupData}] : [{data: this.percentageData}];
		} else {
			this.chartOptions.plugins.tooltip.callbacks.label = undefined;
			this.chartData = this.groupSmallData ? [{data: this.originalGroupData}] : [{data: this.originalData}];
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

	formatDate(resultSet) {
		const dateFormatter = ({ x }) => moment(x).format(this.dateFormat);
		this.chartLabels = resultSet.chartPivot().map(dateFormatter);
	}

	updateTableData(resultSet, pivotConfig) {
		this.tableData = formatColumnDate(
			resultSet.tablePivot(pivotConfig),
			['Referencias.fecha_consulta', 'Referencias.fecha_turno']
		);
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

		this.deleteRepetedRows();

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

	deleteRepetedRows() {
		let ids: number[] = [];
		let table = [];
		let data = [];
		const today: moment.Moment = newMoment();

		this.tableData.forEach(row => {
			const id = row['Referencias.id'];
			if (!ids.includes(id)) {
				ids.push(id);
			}
			this.formatRow(row);
		})

		ids.forEach(id => {
			table.push(this.tableData.filter(row => {
				return id === row['Referencias.id'];
			}))
		})

		table.forEach(arr => {
			let fut = arr.filter(row => {
				const rowDate = buildFullDate(row['Referencias.hora_turno'], momentParse(row['Referencias.fecha_turno'], DateFormat.VIEW_DATE));
				return today.isBefore(rowDate);
			})
			let aux;
			if (fut.length) {
				fut.forEach(row => {
					if (!aux)
						aux = row;
					else {
						const rowDate = buildFullDate(row['Referencias.hora_turno'], momentParse(row['Referencias.fecha_turno'], DateFormat.VIEW_DATE));
						const auxDate = buildFullDate(aux['Referencias.hora_turno'], momentParse(aux['Referencias.fecha_turno'], DateFormat.VIEW_DATE));
						if (rowDate.isBefore(auxDate))
							aux = row;
					}
				})
			}
			else {
				arr.forEach(row => {
					if (!aux)
						aux = row;
					else {
						const rowDate = buildFullDate(row['Referencias.hora_turno'], momentParse(row['Referencias.fecha_turno'], DateFormat.VIEW_DATE));
						const auxDate = buildFullDate(aux['Referencias.hora_turno'], momentParse(aux['Referencias.fecha_turno'], DateFormat.VIEW_DATE));
						if (auxDate.isBefore(rowDate))
							aux = row;
					}
				})
			}
			data.push(aux);
		})

		this.tableData = data;
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
