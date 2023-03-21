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

const parse = (value: string): string => {
	let splitedValue = value.split(',')[0];
	if (isDate(splitedValue)) {
		const month = splitedValue[5] + splitedValue[6];
		splitedValue = MONTHS_OF_YEAR[Number(month) - 1];
	}
	return splitedValue.toString();
}

const isDate = (value: string): boolean => {
	if (moment(value, moment.ISO_8601, true).isValid()) {
		return true;
	}
	return false;
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

	chartLabels: Label[] = [];
	chartData: any[] = [];
	percentageData: any[] = [];
	others: any[] = [];

	nameSelfDeterminationFF: boolean;
	pieSum = 0;
	showPercentage: boolean;
	groupSmallData: boolean;
	showGroupSmallData: boolean;
	noData = false;

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
	numericTitle: string;
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

			if (this.chartType === 'pie')
				this.pieSum = item.series.reduce((partialSum, a) => partialSum + a.value, 0);

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

		this.chartLabels = resultSet.chartPivot(pivotConfig).map((row) => parse(row.x));

		if (this.chartData.length){
			if (this.chartType === 'bar')
				this.chartData.forEach( x => x.label = (x.label.charAt(0).toUpperCase() + x.label.slice(1)).slice(0, -5))

			if (this.chartType === 'pie')
				this.loadPieData();
		}
		else
			this.noData = true;
	}

	loadPieData() {
		this.percentageData = [...this.chartData[0].data];
		this.percentageData.forEach((element, i, array) => array[i] = Math.round((element  * 100 / this.pieSum) * 100) / 100);

		const i = this.percentageData.findIndex(x => x < 1);

		if (i > 1)
			this.showGroupSmallData = true;
		else
			this.showGroupSmallData = false;

		this.showPercentage = false;
		this.groupSmallData = false;
	}

	togglePercentage() {
		this.showPercentage = !this.showPercentage;
		const data = this.chartData[0].data;
		data.forEach(x => {
			if (this.showPercentage === true)
				data[data.indexOf(x)] = (Math.round((x * 100 / this.pieSum) * 100) / 100) + '%';
			else
				data[data.indexOf(x)] = (Math.round((x.slice(0, -1) * this.pieSum) / 100));
		})
	}

	toggleGroupSmallData() {
		this.groupSmallData = !this.groupSmallData;
		const data = this.chartData[0].data;
		if (this.groupSmallData === true)
			this.groupData(data);
		if (this.groupSmallData === false)
			this.unGroupData(data);
	}

	groupData(data: any[]) {
		const i = this.percentageData.findIndex(x => x < 1);
		this.others = data.slice(i);
		let othersSum = this.others.reduce((partialSum, a) => partialSum + a, 0);
		data = data.slice(0, i);
		data.push(othersSum);
		data = data.sort((a,b) => b-a);
		const index = data.indexOf(othersSum);
		for (let i = this.chartLabels.length; i > index; i--) {
			this.chartLabels[i] = this.chartLabels[i-1];
		}
		this.chartLabels[index] = 'Otros';
		this.chartData[0].data = data;
	}

	unGroupData(data: any[]) {
		const i = this.chartLabels.indexOf('Otros');
		this.chartLabels.splice(i, 1);
		data.splice(i, 1);
		this.others.forEach( x => data.push(x));
		data = data.sort((a,b) => b-a);
		this.chartData[0].data = data;
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

		this.numericTitle = resultSet.totalRow()?.x.toUpperCase();

		if (this.numericTitle === "")
			this.numericTitle = "CONSULTAS";

		if (this.numericTitle === "MASCULINO" || this.numericTitle === "FEMENINO")
			this.numericTitle += 'S';
	}
}
