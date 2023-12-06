import { BinaryFilter, Filter, TimeDimension, TimeDimensionGranularity } from "@cubejs-client/core";
import { UiChartDefinitionBo } from "@extensions/components/ui-chart/ui-chart.component";
import { differenceInDays, parseISO } from "date-fns";

export const fixTimeDimensions = (chartDefinition: UiChartDefinitionBo): UiChartDefinitionBo => {
	const timeDimension = extractFirstTimeDimension(chartDefinition.cubeQuery?.timeDimensions);
	if (timeDimension) {
		const newGranularity = calculateGranularity(timeDimension.dimension, chartDefinition.cubeQuery?.filters);
		timeDimension.granularity = newGranularity || timeDimension.granularity;
	}


	return chartDefinition;
}

export const extractFirstTimeDimension = (timeDimensions: TimeDimension[] = []) => !!timeDimensions.length ? timeDimensions[0] : undefined;

const calculateGranularity = (dimension: string, filters: Filter[] = []): TimeDimensionGranularity => {
	const filterForDimesion = filters
		.find((f: BinaryFilter) => f.member === dimension) as BinaryFilter;

	if (filterForDimesion) {
		const [start, end] = filterForDimesion.values;
		const days = differenceInDays(parseISO(end), parseISO(start));

		if (days < 45) return 'day';
	}

	return undefined; // no se pudo calcular
}
