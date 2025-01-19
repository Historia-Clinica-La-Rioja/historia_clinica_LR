
import { MONTHS_OF_YEAR } from '@core/utils/moment.utils';
import { ChartOptions } from 'chart.js';
import {
	// ChartPivotRow,
	ResultSet,
} from '@cubejs-client/core';
import { UiChartDefinitionBo } from '../ui-chart/ui-chart.component';
import { ChartDataService } from "./chart-data.service";
import { isValid, parseISO } from 'date-fns';
// import { extractFirstTimeDimension } from '@extensions/utils/cube-query.utils';


const percent = (total: number) => (value: number) => ((value / total) * 100).toFixed(2);

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
	return isValid(parseISO(value))

}

const DEFAULT_OPTIONS = {
	responsive: true,
	maintainAspectRatio: false,
};

export class ChartService {
	noData = false;
	chartLabels: string[] = [];
	chartData: any[] = [];

	grouping: ChartDataService;

	constructor(
		resultSet: ResultSet,
		public chart: UiChartDefinitionBo,

		public chartOptions: ChartOptions = DEFAULT_OPTIONS,
	) {
		const { chartType, pivotConfig } = chart;
		this.chartData = resultSet.series(pivotConfig).map((item) => {
			if (chartType === 'pie' || chartType === 'doughnut') {
				return {
					data: item.series.map(({ value }) => value),
				};
			}

			if (chartType === 'line' || chartType === 'bar') {
				let title = (pivotConfig.y.length>1) ? item.title.charAt(0).toUpperCase() + item.title.slice(1, -5) : item.title;
				return {
					label: title,
					data: item.series.map(({ value }) => value)
				};
			}

			return {
				label: item.title,
				data: item.series.map(({ value }) => value),
				stack: 'a',
			};
		});



		this.chartLabels = resultSet.chartPivot(pivotConfig).map((row) =>
			isDate(row.x) ? parseDate(row.x, this.getGranularityDate(pivotConfig.x[0])) : row.x);

		this.noData = !this.chartData?.length;

		if(!this.noData) {
			if (chartType === 'pie' || chartType === 'doughnut') {
				this.grouping = new ChartDataService(
					this.chartLabels,
					this.chartData[0].data,
					({labels, data}) => {
						this.chartData = [{ data }];
						this.chartLabels = labels;
					}
				);
				this.addPercent(chartOptions, this.chartData[0].data);
			}
		}
	}

	private addPercent(
		chartOptions: ChartOptions,
		data: any[],
	) {
		// Calcular el total de los valores
		const toPercent = percent(data.reduce((a, b) => a + b, 0));

		const label = (context) => {
			return `${context.label}: ${context.parsed} ( ${toPercent(context.parsed)}% )`;
		};

		chartOptions.plugins = (chartOptions.plugins || {});
		chartOptions.plugins.tooltip = (chartOptions.plugins.tooltip || {});
		chartOptions.plugins.tooltip.callbacks = (chartOptions.plugins.tooltip.callbacks || {});
		chartOptions.plugins.tooltip.callbacks.label = label;
	}


	private getGranularityDate(xConfig : string) : string {
		const x = xConfig.split(".");
		if(x.length > 2 )
			return x[2].toString();
		return null;
	}


}

