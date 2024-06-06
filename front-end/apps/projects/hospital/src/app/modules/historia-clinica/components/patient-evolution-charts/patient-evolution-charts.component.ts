import { Component, Input, } from '@angular/core';
import { Chart } from '@charts/components/chart/chart.component';
import { toChart } from '@historia-clinica/mappers/evolution-charts.mapper';
import { EVOLUTION_CHARTS } from '@historia-clinica/constants/evolution-charts.constants';
import { EvolutionChartOptions } from '../evolution-chart-options/evolution-chart-options.component';
import { AnthropometricGraphicService } from '@api-rest/services/anthropometric-graphic.service';
import { AnthropometricData } from '@historia-clinica/services/patient-evolution-charts.service';
import { AnthropometricDataKey } from '@historia-clinica/modules/ambulatoria/services/datos-antropometricos-nueva-consulta.service';
import { getValuesOfEnum } from '@core/utils/core.utils';

@Component({
	selector: 'app-patient-evolution-charts',
	templateUrl: './patient-evolution-charts.component.html',
	styleUrls: ['./patient-evolution-charts.component.scss']
})
export class PatientEvolutionChartsComponent {

	chart: Chart;
	loadChart = false;
	@Input() patientId: number;
	@Input() anthropometricData: AnthropometricData;

	constructor(
		private readonly anthropometricGraphicService: AnthropometricGraphicService,
	) { }

	buildChart(chartOptions: EvolutionChartOptions) {
		this.loadChart = true;
		const actualValueToGraphic = this.getActualAntropometricDataValueByChart(chartOptions.chart.id);
		this.anthropometricGraphicService.getPercentilesGraphicData(chartOptions, this.patientId, actualValueToGraphic).subscribe(
			chartDefinition => {
				this.chart = toChart(chartDefinition);
				this.loadChart = false;
			})
	}

	private getActualAntropometricDataValueByChart(chartOptionId: number): AnthropometricData | null {
		if (!this.anthropometricData)
			return null;

		const anthropometricDataByEvolutionChart: Map<number, AnthropometricDataKey[]> = this.buildAnthropometricDataByEvolutionChartMap();
		const anthropometricDataKeys = anthropometricDataByEvolutionChart.get(chartOptionId);

		if (!anthropometricDataKeys)
			return null;

		const anthropometricData: AnthropometricData = {};
		anthropometricDataKeys.forEach(anthropometricDataKey => {
			if (this.anthropometricData[anthropometricDataKey])
				anthropometricData[anthropometricDataKey] = this.anthropometricData[anthropometricDataKey];
		});
		return anthropometricData;
	}

	private buildAnthropometricDataByEvolutionChartMap(): Map<number, AnthropometricDataKey[]> {
		const anthropometricDataByEvolutionChartMap = new Map<number, AnthropometricDataKey[]>();
		const evolutionChartsId = getValuesOfEnum(EVOLUTION_CHARTS);
		evolutionChartsId.forEach(evolutionChartId => {
			anthropometricDataByEvolutionChartMap.set(evolutionChartId, AnthropometricDataKeyByEvolutionChart[evolutionChartId])
		});
		return anthropometricDataByEvolutionChartMap;
	}

}

const AnthropometricDataKeyByEvolutionChart = {
	[EVOLUTION_CHARTS.HEIGHT_FOR_AGE]: ['height'],
	[EVOLUTION_CHARTS.WEIGHT_FOR_AGE]: ['weight'],
	[EVOLUTION_CHARTS.WEIGHT_FOR_LENGTH]: ['height', 'weight'],
	[EVOLUTION_CHARTS.WEIGHT_FOR_HEIGHT]: ['height', 'weight'],
	[EVOLUTION_CHARTS.HEAD_CIRCUMFERENCE]: ['headCircumference'],
	[EVOLUTION_CHARTS.BMI_FOR_AGE]: ['height', 'weight'],
}
