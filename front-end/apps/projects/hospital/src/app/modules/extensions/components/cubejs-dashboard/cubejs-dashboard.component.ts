import { Component, Input, OnInit } from '@angular/core';
import { UIComponentDto } from '@extensions/extensions-model';
import {
	ChartDefinitionService,
	FilterValue,
} from '@extensions/services/chart-definition.service';

export interface FilterDefinition {
	member: string;
	operator: string;
}

export interface FilterFormDefinition {
	filter: FilterDefinition;
	type: string;
	label: string;
}

export interface QueryFilters {
	[key: string]: FilterFormDefinition;
}

export interface FilterValues {
	[key: string]: string[];
}

@Component({
	selector: 'app-cubejs-dashboard',
	templateUrl: './cubejs-dashboard.component.html',
	styleUrls: ['./cubejs-dashboard.component.scss']
})
export class CubejsDashboardComponent implements OnInit {

	@Input() content: UIComponentDto[];
	@Input() filters: QueryFilters;

	title: string;
	disableFilter = false;

	private params: FilterValues = {};

	constructor(
		private chartDefinitionService: ChartDefinitionService,
	) { }

	ngOnInit(): void {
		this.chartDefinitionService.next([]);
		this.setTitle(this.content[0].args.query);
	}

	changeValue(key: string, values: string[]) {
		this.params[key] = values;

		const filtersToAdd: FilterValue[] = Object.entries(this.params)
			.map(([key, values]) => ({
				...this.filters[key].filter,
				values,
			}));
		this.chartDefinitionService.next(filtersToAdd);
	}

	setTitle(queryName: string) {
		switch (queryName) {
			case 'cantidadConsultasAmbulatorias': {
				this.title = 'Evolución de consultas del año actual'
				this.disableFilter = true;
				break;
			}
			case 'cantidadConsultasAmbulatoriasEspecialidadProfesional': {
				this.title = 'Consultas por especialidad y profesional del último trimestre'
				this.disableFilter = true;
				break;
			}
			case 'cantidadConsultasPorEspecialidad': {
				this.title = 'Consultas por especialidad'
				break;
			}
			case 'cantidadTurnos': {
				this.title = 'Evolución de turnos del año actual'
				this.disableFilter = true;
				break;
			}
			case 'cantidadTurnosPorEspecialidad': {
				this.title = 'Turnos por especialidad del año actual'
				this.disableFilter = true;
				break;
			}
			case 'cantidadTurnosPorProfesional': {
				this.title = 'Turnos por profesional del año actual'
				this.disableFilter = true;
				break;
			}
			case 'cantidadConsultasTotal': {
				this.title = 'Consultas por género'
				break;
			}
		}
	}
}
