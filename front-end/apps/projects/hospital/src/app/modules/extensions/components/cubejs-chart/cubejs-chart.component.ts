import { Component, Input, OnDestroy } from '@angular/core';
import { ReplaySubject, Subscription } from 'rxjs';
import { ChartDefinitionService } from '@extensions/services/chart-definition.service';
import { ChartDefinitionDto, UIComponentDto } from '@extensions/extensions-model';


const toUIComponentDto = (error: any): UIComponentDto => ({
	type: 'json',
	args: {
		content: error
	}
});
@Component({
	selector: 'app-cubejs-chart',
	templateUrl: './cubejs-chart.component.html',
	styleUrls: ['./cubejs-chart.component.scss']
})
export class CubejsChartComponent implements OnDestroy {

	@Input() dateFormat?: string;
	@Input() showLegend?: true;
	error: UIComponentDto = undefined;
	chartType = new ReplaySubject<any>(1);
	cubeQuery = new ReplaySubject<any>(1);
	pivotConfig = new ReplaySubject<any>(1);

	title: string;
	disableFilter = false;

	@Input() listOnTab: string = null;

	private chartDefinitionSubscription: Subscription;

	constructor(
		private chartDefinitionService: ChartDefinitionService,
	) { }

	@Input()
	set query(queryName: string) {
		if (!queryName) {
			console.warn('Chart with undefined queryStream');
			return;
		}
		this.chartDefinitionSubscription = this.chartDefinitionService.queryStream$(queryName).subscribe(
			(queryStream: ChartDefinitionDto) => {
				this.error = undefined;
				this.chartType.next(queryStream.chartType);
				this.cubeQuery.next(queryStream.cubeQuery);
				this.pivotConfig.next(queryStream.pivotConfig);
				this.setTitle(queryName);
				this.cleanFilters(queryStream);
			},
			(error: any) => this.error = toUIComponentDto(error),
		)

	}

	ngOnDestroy() {
		this.chartDefinitionSubscription?.unsubscribe();
	}

	cleanFilters(queryStream) {
		let filters = [...queryStream.cubeQuery.filters];
		if (this.disableFilter) {
			filters = [];
		} else {
			for (let i = filters.length - 1; i >= 0; i--) {
				let splitedQuery = queryStream.cubeQuery.measures[0] ? queryStream.cubeQuery.measures[0].split('.') : queryStream.cubeQuery.dimensions[0].split('.');
				let splitedFilter = filters[i].member ? filters[i].member.split('.') : filters[i].dimension.split('.');
				if (splitedQuery[0] !== splitedFilter[0]) {
					filters.splice(i, 1);
				}
			}
		}
		queryStream.cubeQuery.filters = [...filters];
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
