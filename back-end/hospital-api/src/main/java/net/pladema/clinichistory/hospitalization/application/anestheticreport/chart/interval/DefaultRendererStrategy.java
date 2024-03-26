package net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.interval;

import java.text.SimpleDateFormat;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.springframework.stereotype.Component;

@Component
public class DefaultRendererStrategy extends IntervalFormatStrategy {

    public DefaultRendererStrategy() {
        renderer = new XYLineAndShapeRenderer();
        dateFormat = new SimpleDateFormat("HH:mm");
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
