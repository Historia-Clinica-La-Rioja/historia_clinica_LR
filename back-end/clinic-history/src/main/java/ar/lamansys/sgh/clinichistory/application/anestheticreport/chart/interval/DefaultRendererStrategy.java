package ar.lamansys.sgh.clinichistory.application.anestheticreport.chart.interval;

import java.text.SimpleDateFormat;
import lombok.NonNull;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.springframework.stereotype.Component;

@Component
public class DefaultRendererStrategy extends IntervalFormatStrategy {

    public DefaultRendererStrategy() {
        dateFormat = new SimpleDateFormat("HH:mm");
    }

    @NonNull
    public XYLineAndShapeRenderer getRenderer() {
        return new XYLineAndShapeRenderer();
    }

    @Override
    public void setDotValuesLabels(XYLineAndShapeRenderer renderer) {
        renderer.setDefaultItemLabelsVisible(true);
        renderer.setDefaultItemLabelGenerator(getXyItemLabelGenerator());
        renderer.setItemLabelAnchorOffset(10.);
    }

    @NonNull
    private XYItemLabelGenerator getXyItemLabelGenerator() {
        return (dataset, series, item) -> {
            TimeSeriesCollection tsDataset = (TimeSeriesCollection) dataset;
            TimeSeries ts = tsDataset.getSeries(series);
            Number value = ts.getDataItem(item).getValue();
            return String.valueOf(value.intValue());
        };
    }
}
