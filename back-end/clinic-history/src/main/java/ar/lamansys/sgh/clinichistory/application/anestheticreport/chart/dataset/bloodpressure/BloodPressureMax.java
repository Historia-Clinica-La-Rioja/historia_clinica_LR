package ar.lamansys.sgh.clinichistory.application.anestheticreport.chart.dataset.bloodpressure;

import java.awt.Shape;
import lombok.NoArgsConstructor;
import ar.lamansys.sgh.clinichistory.application.anestheticreport.chart.dataset.BloodPressure;
import ar.lamansys.sgh.clinichistory.application.anestheticreport.chart.dataset.Series;
import ar.lamansys.sgh.clinichistory.application.anestheticreport.chart.utils.ShapesGenerator;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.ui.TextAnchor;
import org.springframework.stereotype.Component;

import static ar.lamansys.sgh.clinichistory.application.anestheticreport.chart.dataset.BloodPressure.POINT_PLACE_OFFSET;

@NoArgsConstructor
@Component
public class BloodPressureMax implements Series {

    @Override
    public Shape getShape() {
        return ShapesGenerator.createCaretDown(SHAPE_OFFSET + POINT_PLACE_OFFSET, SHAPE_OFFSET, SHAPE_SIZE, SHAPE_SIZE);
    }

    @Override
    public ItemLabelPosition getPositionLabel() {
        return new ItemLabelPosition(
                ItemLabelAnchor.OUTSIDE4, TextAnchor.BOTTOM_LEFT, TextAnchor.CENTER, 0.
        );
    }

    @Override
    public String getLabel() {
        return BloodPressure.LABEL;
    }

    @Override
    public String getName() {
        return "Tensión arterial Max.";
    }
}
