import { Component, Input, OnDestroy, OnInit } from '@angular/core';
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
export class CubejsChartComponent implements OnDestroy, OnInit {

	@Input() dateFormat?: string;
	@Input() showLegend?: true;
	@Input() chartDefinitionService: ChartDefinitionService;
	@Input() query: string;
	error: UIComponentDto = undefined;
	chartType = new ReplaySubject<any>(1);
	cubeQuery = new ReplaySubject<any>(1);
	pivotConfig = new ReplaySubject<any>(1);
	title: string;

	@Input() listOnTab: string = null;

	private chartDefinitionSubscription: Subscription;

	constructor() {
	}

	ngOnInit(): void {
		if (!this.query) {
			console.warn('Chart with undefined queryStream');
			return;
		}
		this.chartDefinitionSubscription = this.chartDefinitionService.queryStream$(this.query).subscribe(
			(queryStream: ChartDefinitionDto) => {
				this.error = undefined;
				this.chartType.next(queryStream.chartType);
				this.cubeQuery.next(queryStream.cubeQuery);
				this.pivotConfig.next(queryStream.pivotConfig);
				this.setTitle(this.query);
			},
			(error: any) => this.error = toUIComponentDto(error),
		)
	}

	ngOnDestroy() {
		this.chartDefinitionSubscription?.unsubscribe();
	}

	setTitle(queryName: string) {
		switch (queryName) {
			case 'cantidadConsultasAmbulatorias': {
				this.title = 'Evolución de consultas del año actual'
				break;
			}
			case 'cantidadConsultasAmbulatoriasEspecialidadProfesional': {
				this.title = 'Consultas por especialidad y profesional del último trimestre'
				break;
			}
			case 'cantidadConsultasPorEspecialidad': {
				this.title = 'Consultas por especialidad'
				break;
			}
			case 'cantidadTurnos': {
				this.title = 'Evolución de turnos del año actual'
				break;
			}
			case 'cantidadTurnosPorEspecialidad': {
				this.title = 'Turnos por especialidad del año actual'
				break;
			}
			case 'cantidadTurnosPorProfesional': {
				this.title = 'Turnos por profesional del año actual'
				break;
			}
		}
	}
}
