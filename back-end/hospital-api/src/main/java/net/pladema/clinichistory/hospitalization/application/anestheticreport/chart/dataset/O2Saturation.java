package net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.dataset;

import java.awt.Shape;
import java.text.DecimalFormat;
import net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.utils.ShapesGenerator;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.TextAnchor;
import org.springframework.stereotype.Component;

@Component
public class O2Saturation extends ChartDataset implements Series {

    public O2Saturation() {
        super.minRange = 0;
        super.maxRange = 100;
    }

    @Override
    public String getLabel() {
        return "SO2";
    }

    @Override
    public Integer getIndexFromDatasetList() {
        return 2;
    }

    @Override
    public NumberTickUnit getTickUnit() {
        return new NumberTickUnit(5., new DecimalFormat("0"));
    }

    @Override
    public Shape getShape() {
        return ShapesGenerator.createSquare(SHAPE_OFFSET, SHAPE_OFFSET, SHAPE_SIZE);
    }

    @Override
    public ItemLabelPosition getPositionLabel() {
        return new ItemLabelPosition(
                ItemLabelAnchor.OUTSIDE3, TextAnchor.TOP_LEFT, TextAnchor.CENTER, 0.
        );
    }

    @Override
    public String getName() {
        return "Saturación O2";
    }

    @Override
    public Integer getIndexRenderingOrder() {
        return 1;
    }

    @Override
    public void setDots(XYLineAndShapeRenderer renderer) {
        Series.super.setSeries(0, renderer);
    }

}
