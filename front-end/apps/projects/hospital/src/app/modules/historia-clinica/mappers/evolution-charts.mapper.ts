import { GraphicDatasetInfoDto, AnthropometricGraphicDataDto, EAnthropometricGraphicRange } from "@api-rest/api-model";
import { Chart, DataSet } from "@charts/components/chart/chart.component";
import { CHARTS_LABELS_COLORS, LABELS } from "@historia-clinica/constants/evolution-charts.constants";
import { ChartOptions } from "chart.js";

const EVOLUTION_CHART_TYPE = 'line';
const GRID_STYLE_BOLD = 'rgba(0, 0, 0, 0.2)';
const GRID_STYLE = 'rgba(0, 0, 0, 0.1)';
const LINE_WIDTH_BOLD = 2;
const LINE_WIDTH = 1;
const LEGEND_POSITION = "right";
const MONTH = "mes";
const YEAR = "año";
const EVOLUTION = "Evolución";
const Z_SCORE = "Puntaje Z:";

const toDataset = (dataSet: GraphicDatasetInfoDto): DataSet => {
	return {
		label: dataSet.label.value,
		data: dataSet.intersections,
		borderColor: CHARTS_LABELS_COLORS[dataSet.label.id],
		...(!isEvolution(dataSet.label.id) && { borderWidth: 1 }),
		pointBackgroundColor: CHARTS_LABELS_COLORS[dataSet.label.id],
		...(!isEvolution(dataSet.label.id) && { pointRadius: 0 }),
		fill: false,
	}

	function isEvolution(labelId: number): boolean {
		return labelId === LABELS.EVOLUTION;
	}
}

const toTitle = (title: string): Object => {
	return { display: true, text: title }
}

const toScaleY = (title: string): Object => {
	return { title: toTitle(title) }
}

const isNotAChartWithRangeAge = (scale: EAnthropometricGraphicRange): boolean => {
	return scale === EAnthropometricGraphicRange.WEIGHT_FOR_LENGTH || scale === EAnthropometricGraphicRange.WEIGHT_FOR_HEIGHT;
}

const getColorByLabelAndScale = (label: string, scale: EAnthropometricGraphicRange): string => {
	if (isNotAChartWithRangeAge)
		return GRID_STYLE;

	if (scale === EAnthropometricGraphicRange.NINETEEN_YEARS)
		return label !== "" ? GRID_STYLE_BOLD : GRID_STYLE;

	if (scale === EAnthropometricGraphicRange.FIVE_YEARS)
		return label.includes(YEAR) ? GRID_STYLE_BOLD : GRID_STYLE;

	return label.includes(MONTH) ? GRID_STYLE_BOLD : GRID_STYLE;
};

const getWidthByLabelAndScale = (label: string, scale: EAnthropometricGraphicRange): number => {
	if (isNotAChartWithRangeAge(scale))
		return LINE_WIDTH;

	if (scale === EAnthropometricGraphicRange.NINETEEN_YEARS)
		return label !== "" ? LINE_WIDTH_BOLD : LINE_WIDTH;

	if (scale === EAnthropometricGraphicRange.FIVE_YEARS)
		return label.includes(YEAR) ? LINE_WIDTH_BOLD : LINE_WIDTH;

	return label.includes(MONTH) ? LINE_WIDTH_BOLD : LINE_WIDTH;
};

const toScaleX = (chartDefinition: AnthropometricGraphicDataDto): Object => {
	return {
		grid: {
			color: (context) => getColorByLabelAndScale(context.tick.label, chartDefinition.graphicRange),
			lineWidth: (context) => getWidthByLabelAndScale(context.tick.label, chartDefinition.graphicRange)
		},
		ticks: {
			callback: function (index) {
				return chartDefinition.xaxisRangeLabels[index];
			}
		},
		title: toTitle(chartDefinition.xaxisLabel)
	}
}

const toTooltip = (chartDefinition: AnthropometricGraphicDataDto): Object => {
	return {
		callbacks: {
			title: function (tooltipItems) {
				const indexOfRangeLabel = tooltipItems[0].parsed.x;
				return `${chartDefinition.xaxisRangeLabels[indexOfRangeLabel]}`;
			},
			label: function (context) {
				const labelWithValue = `${context.dataset.label}: ${context.formattedValue}`;
				if (!chartDefinition.evolutionZScoreValues)
					return labelWithValue;

				const dataSetLabel = context.dataset.label;
				const zScoreValue = chartDefinition.evolutionZScoreValues[context.dataIndex];
				const zScore = `${Z_SCORE} ${zScoreValue}`;
				return dataSetLabel.includes(EVOLUTION) ? `${labelWithValue} - ${zScore}` : `${labelWithValue}`;
			}
		}
	};
};

const toEvolutionChartsOptions = (chartDefinition: AnthropometricGraphicDataDto): ChartOptions => {
	return {
		responsive: true,
		maintainAspectRatio: false,
		plugins: {
			legend: {
				position: LEGEND_POSITION,
				labels: { usePointStyle: true }
			},
			tooltip: toTooltip(chartDefinition)
		},
		scales: {
			x: toScaleX(chartDefinition),
			y: toScaleY(chartDefinition.yaxisLabel)
		},
	}
}

export const toChart = (chartDefinition: AnthropometricGraphicDataDto): Chart => {
	return {
		type: EVOLUTION_CHART_TYPE,
		labels: chartDefinition.xaxisRange,
		dataSets: chartDefinition.datasetInfo.map(dataSet => toDataset(dataSet)),
		options: toEvolutionChartsOptions(chartDefinition)
	}
}
