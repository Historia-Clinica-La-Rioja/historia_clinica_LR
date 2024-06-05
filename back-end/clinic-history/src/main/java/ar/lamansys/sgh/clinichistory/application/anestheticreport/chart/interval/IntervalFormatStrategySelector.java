package ar.lamansys.sgh.clinichistory.application.anestheticreport.chart.interval;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class IntervalFormatStrategySelector {

    public final int LIMIT = 12;

    private final DefaultRendererStrategy defaultRendererStrategy;
    private final RendererToPlotManyPointsStrategy rendererToPlotManyPointsStrategy;

    public IntervalFormatStrategy apply(int numberOfMeasurements) {
        return numberOfMeasurements <= LIMIT ? defaultRendererStrategy : rendererToPlotManyPointsStrategy;
    }
}
