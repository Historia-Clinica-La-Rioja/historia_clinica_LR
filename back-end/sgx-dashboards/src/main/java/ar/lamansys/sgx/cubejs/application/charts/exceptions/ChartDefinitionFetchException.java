package ar.lamansys.sgx.cubejs.application.charts.exceptions;

public class ChartDefinitionFetchException extends RuntimeException {

	public final String chartName;

	public ChartDefinitionFetchException(String chartName, Throwable cause) {
		super(cause.getMessage(), cause);
		this.chartName = chartName;
	}
}
