package net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.series;

import java.awt.Shape;
import java.text.DecimalFormat;
import net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.utils.ShapesGenerator;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ui.TextAnchor;
import org.springframework.stereotype.Component;

@Component
public class O2Saturation extends Series {

    public O2Saturation() {
        super.minRange = 0;
        super.maxRange = 100;
    }

    @Override
    protected String getLabel() {
        return "SO2";
    }

    @Override
    protected int getIndexAxisY() {
        return 1;
    }

    @Override
    protected int getSerieNumber() {
        return 3;
    }

    @Override
    protected NumberTickUnit getTickUnit() {
        return new NumberTickUnit(3., new DecimalFormat("0"));
    }

    @Override
    protected Shape getShape() {
        return ShapesGenerator.createSquare(SHAPE_OFFSET, SHAPE_OFFSET, SHAPE_SIZE);
    }

    @Override
    public void mapSeriesWithAxis(XYPlot plot) {
        plot.mapDatasetToRangeAxis(this.getSerieNumber(), 2);
    }

    @Override
    protected ItemLabelPosition getPositionLabel() {
        return new ItemLabelPosition(
                ItemLabelAnchor.OUTSIDE3, TextAnchor.TOP_LEFT, TextAnchor.CENTER, 0.
        );
    }

    @Override
    public String getName() {
        return "Saturación O2";
    }

}
