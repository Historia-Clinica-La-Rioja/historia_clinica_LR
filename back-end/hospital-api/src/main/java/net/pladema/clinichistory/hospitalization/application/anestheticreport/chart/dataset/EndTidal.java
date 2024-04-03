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
public class EndTidal extends ChartDataset implements Series {

    public EndTidal() {
        super.minRange = 0;
        super.maxRange = 240;
    }

    @Override
    public String getLabel() {
        return "ET";
    }

    @Override
    public Integer getIndexFromDatasetList() {
        return 3;
    }

    @Override
    public NumberTickUnit getTickUnit() {
        return new NumberTickUnit(10., new DecimalFormat("0"));
    }

    @Override
    public Shape getShape() {
        return ShapesGenerator.createTriangle(SHAPE_OFFSET - 1, SHAPE_OFFSET - 1, SHAPE_SIZE + 2, SHAPE_SIZE);
    }

    @Override
    public ItemLabelPosition getPositionLabel() {
        return new ItemLabelPosition(
                ItemLabelAnchor.OUTSIDE3, TextAnchor.TOP_LEFT, TextAnchor.CENTER, 0.
        );
    }

    @Override
    public String getName() {
        return "End Tidal";
    }

    @Override
    public Integer getIndexRenderingOrder() {
        return 0;
    }

    @Override
    public void setDots(XYLineAndShapeRenderer renderer) {
        Series.super.setSeries(0, renderer);
    }
}
