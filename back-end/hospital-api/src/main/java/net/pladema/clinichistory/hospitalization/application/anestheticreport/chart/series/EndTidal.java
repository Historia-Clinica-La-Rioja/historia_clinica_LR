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
public class EndTidal extends Series {
    public EndTidal() {
        super.minRange = 0;
        super.maxRange = 240;
    }

    @Override
    protected String getLabel() {
        return "ET";
    }

    @Override
    protected int getIndexAxisY() {
        return 0;
    }

    @Override
    protected int getSerieNumber() {
        return 4;
    }

    @Override
    protected NumberTickUnit getTickUnit() {
        return new NumberTickUnit(10., new DecimalFormat("0"));
    }

    @Override
    protected Shape getShape() {
        return ShapesGenerator.createTriangle(SHAPE_OFFSET - 1, SHAPE_OFFSET - 1,SHAPE_SIZE + 2, SHAPE_SIZE);
    }

    @Override
    public void mapSeriesWithAxis(XYPlot plot) {
        plot.mapDatasetToRangeAxis(this.getSerieNumber(), 3);
    }

    @Override
    protected ItemLabelPosition getPositionLabel() {
        return new ItemLabelPosition(
                ItemLabelAnchor.OUTSIDE3, TextAnchor.TOP_LEFT, TextAnchor.CENTER, 0.
        );
    }

    @Override
    public String getName() {
        return "End Tidal";
    }
}
