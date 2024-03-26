package net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.series;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Shape;
import java.security.SecureRandom;
import java.text.NumberFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jfree.chart.axis.AxisLabelLocation;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.Range;
import org.jfree.data.time.TimeSeries;

@Getter
@NoArgsConstructor
public abstract class Series {

    private final Color BLACK = Color.BLACK;
    private final Font ROBOTO = new Font("Roboto", Font.PLAIN, 12);

    protected final int SHAPE_SIZE = 6;
    protected final int SHAPE_OFFSET = 4;

    protected int minRange;
    protected int maxRange;

    public void setRange(XYPlot plot) {
        NumberAxis rangeAxis = new NumberAxis(this.getLabel());

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

        plot.setRangeAxis(this.getIndexAxisY(), rangeAxis);
        plot.setRangeAxisLocation(this.getIndexAxisY(), AxisLocation.BOTTOM_OR_LEFT);
    }

    public void setDots(XYLineAndShapeRenderer renderer) {
        renderer.setSeriesPaint(this.getSerieNumber(), BLACK);
        renderer.setSeriesShape(this.getSerieNumber(), this.getShape());
        renderer.setSeriesStroke(this.getSerieNumber(), new BasicStroke());
        renderer.setSeriesPositiveItemLabelPosition(this.getSerieNumber(), this.getPositionLabel());
        renderer.setSeriesNegativeItemLabelPosition(this.getSerieNumber(), this.getPositionLabel());
    }

    public String getLegendName() {
        return String.format("%s (%s)", this.getName(), this.getLabel());
    }

    public TimeSeries createTimeSeries() {
        return new TimeSeries(this.getLegendName());
    }

    protected abstract String getLabel();

    protected abstract int getIndexAxisY();

    protected abstract int getSerieNumber();

    protected abstract NumberTickUnit getTickUnit();

    protected Range getRange() {
        return new Range(getMinRange(), getMaxRange());
    };

    protected abstract Shape getShape();

    public abstract void mapSeriesWithAxis(XYPlot plot);

    protected abstract ItemLabelPosition getPositionLabel();

    public abstract String getName();

    public int getPosibleRandomValue() {
        return new SecureRandom().nextInt(getMaxRange() - getMinRange()) + getMinRange();
    }

}
