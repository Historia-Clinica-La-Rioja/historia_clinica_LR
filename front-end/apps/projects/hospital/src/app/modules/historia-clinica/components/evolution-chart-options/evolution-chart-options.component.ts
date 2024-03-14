import { Component, EventEmitter, Input, Output } from '@angular/core';
import { EAnthropometricGraphicOption, EAnthropometricGraphicType } from '@api-rest/api-model';

@Component({
	selector: 'app-evolution-chart-options',
	templateUrl: './evolution-chart-options.component.html',
	styleUrls: ['./evolution-chart-options.component.scss']
})
export class EvolutionChartOptionsComponent {

	selectedChart: EAnthropometricGraphicOption;
	@Input() patientId: number;
	@Output() selectedEvolutionChartOptions = new EventEmitter<EvolutionChartOptions>();

	constructor() { }

	emitChartOptions(type: EAnthropometricGraphicType) {
		const evolutionChartOptions: EvolutionChartOptions = { chart: this.selectedChart, type }
		this.selectedEvolutionChartOptions.emit(evolutionChartOptions);
	}
}

export interface EvolutionChartOptions {
	chart: EAnthropometricGraphicOption
	type: EAnthropometricGraphicType
}
