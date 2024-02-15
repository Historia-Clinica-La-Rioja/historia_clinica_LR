import { Component, Input, OnInit } from '@angular/core';
import { flattenColumns, getDisplayedColumns } from '../query-renderer/utils';
import { PivotConfig, ResultSet } from '@cubejs-client/core';
import * as moment from 'moment';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { AppFeature } from '@api-rest/api-model';
import { buildFullDate, DateFormat, momentParse, newMoment } from '@core/utils/moment.utils';


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
	set params({resultSet, pivotConfig}: QueryTableParams) {
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

	deleteRepetedRows() {
		let ids: number[] = [];
		let table = [];
		let data = [];
		const today: moment.Moment = newMoment();

		if (this.displayedColumns.includes("Referencias.id")) {
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
}

export interface QueryTableParams {
	resultSet: ResultSet;
	pivotConfig: PivotConfig;
}
