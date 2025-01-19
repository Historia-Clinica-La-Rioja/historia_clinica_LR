package ar.lamansys.sgh.clinichistory.application.anestheticreport.chart.dataset;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.TimeSeries;

public interface Series {

    int SHAPE_SIZE = 6;
    int SHAPE_OFFSET = 4;

    Shape getShape();
    ItemLabelPosition getPositionLabel();
    String getLabel();
    String getName();

    default String getLegendName() {
        return String.format("%s (%s)", this.getName(), this.getLabel());
    }

    default TimeSeries createTimeSeries() {
        return new TimeSeries(this.getLegendName());
    }

    default void setSeries(int series, XYLineAndShapeRenderer renderer) {
        renderer.setSeriesPaint(series, Color.BLACK);
        renderer.setSeriesShape(series, this.getShape());
        renderer.setSeriesStroke(series, new BasicStroke());
        renderer.setSeriesPositiveItemLabelPosition(series, this.getPositionLabel());
        renderer.setSeriesNegativeItemLabelPosition(series, this.getPositionLabel());
        renderer.setDefaultLinesVisible(false);
        renderer.setAutoPopulateSeriesShape(false);
        renderer.setAutoPopulateSeriesPaint(false);
    }

}
