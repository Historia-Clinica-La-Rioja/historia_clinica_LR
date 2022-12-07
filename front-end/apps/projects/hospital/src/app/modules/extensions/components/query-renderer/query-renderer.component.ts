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
import { buildFullDate, DateFormat, momentParse, momentParseDate, momentParseTime, newMoment } from '@core/utils/moment.utils';

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

const parseIfDate = (value: string): string => {
	return value.substring(0, 10);
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
		if (row['Referencias.estado_turno'] === "Cancelado"){
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
}
