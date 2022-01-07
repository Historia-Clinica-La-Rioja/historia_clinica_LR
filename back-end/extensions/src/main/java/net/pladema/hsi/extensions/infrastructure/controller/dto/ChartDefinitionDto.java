package net.pladema.hsi.extensions.infrastructure.controller.dto;

public class ChartDefinitionDto {
	public final Object cubeQuery;
	public final String chartType;
	public final Object pivotConfig;

	public ChartDefinitionDto(
			Object cubeQuery,
			String chartType,
			Object pivotConfig
	) {
		this.cubeQuery = cubeQuery;
		this.chartType = chartType;
		this.pivotConfig = pivotConfig;
	}

}
