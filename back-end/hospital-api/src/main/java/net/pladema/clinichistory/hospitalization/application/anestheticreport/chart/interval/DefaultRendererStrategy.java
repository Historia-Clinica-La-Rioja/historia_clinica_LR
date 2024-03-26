package net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.interval;

import java.text.SimpleDateFormat;
import lombok.RequiredArgsConstructor;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DefaultRendererStrategy implements IntervalFormatStrategy {

    private static final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

    @Override
    public XYLineAndShapeRenderer getRenderer() {
        return renderer;
    }

    @Override
    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    @Override
    public void setDotValuesLabels(XYLineAndShapeRenderer renderer) {
        renderer.setDefaultItemLabelsVisible(true);
        renderer.setDefaultItemLabelGenerator((dataset, series, item) -> {
            TimeSeriesCollection tsDataset = (TimeSeriesCollection) dataset;
            TimeSeries ts = tsDataset.getSeries(series);
            Number value = ts.getDataItem(item).getValue();
            return String.valueOf(value.intValue());
        });

        renderer.setItemLabelAnchorOffset(10.);
    }
}
