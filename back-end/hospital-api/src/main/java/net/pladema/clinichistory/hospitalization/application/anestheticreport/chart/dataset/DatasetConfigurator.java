package net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.dataset;

import java.awt.Color;
import java.awt.Font;
import java.security.SecureRandom;
import java.text.NumberFormat;
import org.jfree.chart.axis.AxisLabelLocation;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.Range;

public interface DatasetConfigurator {

    Color BLACK = Color.BLACK;
    Font ROBOTO = new Font("Roboto", Font.PLAIN, 12);

    String getLabel();

    Integer getIndexFromDatasetList();

    NumberTickUnit getTickUnit();

    Integer getIndexRenderingOrder();

    Integer getMinRange();

    Integer getMaxRange();

    void setDots(XYLineAndShapeRenderer renderer);

    default void setRange(XYPlot plot) {
        NumberAxis rangeAxis = new NumberAxis(getLabel());

        rangeAxis.setLabelLocation(AxisLabelLocation.HIGH_END);
        rangeAxis.setLabelAngle(0.25 * Math.PI);
        rangeAxis.setLabelInsets(new RectangleInsets(0, 0, 0, 0));
        rangeAxis.setLabelPaint(BLACK);
        rangeAxis.setLabelFont(ROBOTO);

        rangeAxis.setNumberFormatOverride(NumberFormat.getNumberInstance());
        rangeAxis.setTickLabelPaint(BLACK);
        rangeAxis.setTickMarkPaint(BLACK);
        rangeAxis.setTickMarksVisible(false);
        rangeAxis.setTickLabelInsets(new RectangleInsets(2, 2, 2, 2));
        rangeAxis.setTickLabelFont(ROBOTO);

        rangeAxis.setAxisLinePaint(BLACK);
        rangeAxis.setRange(this.getRange());
        rangeAxis.setTickUnit(this.getTickUnit());

        plot.setRangeAxis(this.getIndexRenderingOrder(), rangeAxis);
        plot.setRangeAxisLocation(this.getIndexRenderingOrder(), AxisLocation.BOTTOM_OR_LEFT);
    }

    default Integer getTotalSeries() {
        return 1;
    }

    default void mapDatasetWithAxisX(XYPlot plot) {
        plot.mapDatasetToDomainAxis(getIndexFromDatasetList(), 0);
    }

    default void mapDatasetWithAxisY(XYPlot plot) {
        plot.mapDatasetToRangeAxis(getIndexFromDatasetList(), getIndexRenderingOrder());
    }

    default Range getRange() {
        return new Range(getMinRange(), getMaxRange());
    }

    default int getPosibleRandomValue() {
        return new SecureRandom().nextInt(getMaxRange() - getMinRange()) + getMinRange();
    }
}
