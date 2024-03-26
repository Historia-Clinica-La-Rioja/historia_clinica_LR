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
public class Pulse extends Series {

    public Pulse() {
        super.minRange = 70;
        super.maxRange = 100;
    }

    @Override
    protected String getLabel() {
        return "P";
    }

    @Override
    protected int getIndexAxisY() {
        return 2;
    }

    @Override
    protected int getSerieNumber() {
        return 2;
    }

    @Override
    protected NumberTickUnit getTickUnit() {
        return new NumberTickUnit(2., new DecimalFormat("0"));
    }

    @Override
    protected Shape getShape() {
        return ShapesGenerator.createCircle(SHAPE_OFFSET, SHAPE_OFFSET, SHAPE_SIZE);
    }

    @Override
    public void mapSeriesWithAxis(XYPlot plot) {
        plot.mapDatasetToRangeAxis(this.getSerieNumber(), 1);
    }

    @Override
    protected ItemLabelPosition getPositionLabel() {
        return new ItemLabelPosition(
                ItemLabelAnchor.OUTSIDE3, TextAnchor.TOP_LEFT, TextAnchor.CENTER, 0.
        );
    }

    @Override
    public String getName() {
        return "Pulso";
    }

}
