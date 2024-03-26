package net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.series;

import java.awt.Shape;
import lombok.NoArgsConstructor;
import net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.utils.ShapesGenerator;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.ui.TextAnchor;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Component
public class BloodPressureMax extends BloodPressure {

    @Override
    protected int getSerieNumber() {
        return 1;
    }

    @Override
    protected Shape getShape() {
        return ShapesGenerator.createCaretDown(SHAPE_OFFSET + POINT_PLACE_OFFSET, SHAPE_OFFSET, SHAPE_SIZE, SHAPE_SIZE);
    }

    @Override
    protected ItemLabelPosition getPositionLabel() {
        return new ItemLabelPosition(
                ItemLabelAnchor.OUTSIDE4, TextAnchor.BOTTOM_LEFT, TextAnchor.CENTER, 0.
        );
    }

    @Override
    public String getName() {
        return "Tensión arterial Max.";
    }
}
