package ar.lamansys.sgh.clinichistory.application.anestheticreport.chart.dataset;

import java.text.DecimalFormat;
import ar.lamansys.sgh.clinichistory.application.anestheticreport.chart.dataset.bloodpressure.BloodPressureMax;
import ar.lamansys.sgh.clinichistory.application.anestheticreport.chart.dataset.bloodpressure.BloodPressureMin;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.springframework.stereotype.Component;

@Component
public class BloodPressure extends ChartDataset {

    public static final String LABEL = "TA";
    private final BloodPressureMin bloodPressureMin;
    private final BloodPressureMax bloodPressureMax;

    public static final int POINT_PLACE_OFFSET = 4;

    public BloodPressure(BloodPressureMin bloodPressureMin, BloodPressureMax bloodPressureMax) {
        this.bloodPressureMin = bloodPressureMin;
        this.bloodPressureMax = bloodPressureMax;
        super.minRange = 20;
        super.maxRange = 140;
    }

    @Override
    public String getLabel() {
        return LABEL;
    }

    @Override
    public Integer getIndexFromDatasetList() {
        return 0;
    }

    @Override
    public NumberTickUnit getTickUnit() {
        return new NumberTickUnit(20., new DecimalFormat("0"));
    }

    @Override
    public Integer getIndexRenderingOrder() {
        return 3;
    }

    @Override
    public void setDots(XYLineAndShapeRenderer renderer) {
        bloodPressureMin.setSeries(0, renderer);
        bloodPressureMax.setSeries(1, renderer);
    }

    @Override
    public Integer getTotalSeries() {
        return 2;
    }
}
